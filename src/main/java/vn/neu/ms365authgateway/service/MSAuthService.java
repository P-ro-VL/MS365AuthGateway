package vn.neu.ms365authgateway.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.neu.ms365authgateway.api.ApiCallException;
import vn.neu.ms365authgateway.dto.request.AuthRequest;
import vn.neu.ms365authgateway.dto.response.AuthResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class MSAuthService {

    public static final String MS365_LOGIN_URL = "https://lms.neu.edu.vn/login/index.php";

    final StudentInfoService studentInfoService;

    final AtomicReference<RestTemplate> restTemplate = new AtomicReference<>();
    public AuthResponse auth(AuthRequest request) throws ApiCallException {
        try {
            restTemplate.set(new RestTemplate(new HttpComponentsClientHttpRequestFactory()));
            String loginToken = _getMS365LoginToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            Map<String, String> params = new LinkedHashMap<>();
            params.put("anchor", "");
            params.put("logintoken", loginToken);
            params.put("username", request.getId());
            params.put("password", request.getPassword());

            StringBuilder formBody = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!formBody.isEmpty()) formBody.append("&");
                formBody.append(entry.getKey()).append("=").append(entry.getValue());
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(formBody.toString(), headers);

            ResponseEntity<String> loginResponse = restTemplate.get().exchange(
                    MS365_LOGIN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String response = loginResponse.getBody();
            if((loginResponse.getStatusCode().is3xxRedirection() || loginResponse.getStatusCode().is2xxSuccessful()) && !response.contains("Invalid login")) {
                Map<String, Object> userInfo = studentInfoService.getUserInfo(request.getId());

                return AuthResponse.builder()
                        .result(true)
                        .build();
            } else {
                return AuthResponse.builder()
                        .result(false)
                        .message("Thông tin đăng nhập không chính xác")
                        .build();
            }
        } catch (Exception ex) {
            throw new ApiCallException("Authentication Failed. (" + ex.getMessage() + ")", HttpStatus.UNAUTHORIZED);
        }
    }

    private String _getMS365LoginToken() {
        ResponseEntity<String> response = restTemplate.get().getForEntity(MS365_LOGIN_URL, String.class);

        if (response.getStatusCode().is3xxRedirection() || response.getStatusCode().is2xxSuccessful()) {
            String html = response.getBody();

            Document doc = Jsoup.parse(html);
            Element input = doc.selectFirst("input[name=logintoken]");

            if (input != null) {
                return input.attr("value");
            } else {
                throw new RuntimeException("Failed to fetch login token");
            }
        } else {
            throw new RuntimeException("Unknown: " + response.getStatusCode());
        }
    }

}
