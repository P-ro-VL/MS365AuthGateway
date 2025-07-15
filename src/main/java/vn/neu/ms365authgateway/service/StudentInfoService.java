package vn.neu.ms365authgateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.neu.ms365authgateway.api.ApiCallException;
import vn.neu.ms365authgateway.config.JacksonConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private static final String API_URL = "https://api.neu.edu.vn/api/StudentInfo/";

    private final JacksonConfig objectMapper;

    public Map<String, Object> getUserInfo(String userId) throws ApiCallException {
        try {
            String urlString = API_URL + userId;
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseBody.append(line);
            }
            in.close();
            connection.disconnect();

            JsonNode root = objectMapper.objectMapper().readTree(responseBody.toString());

            // Transforming to Map<String, Object> with UserModel field names
            Map<String, Object> result = new HashMap<>();
            result.put("id", root.path("MaSV").asText());
            result.put("firstName", root.path("Ten").asText());
            result.put("lastName", root.path("HoLot").asText());
            result.put("fullName", root.path("HoLot").asText() + " " + root.path("Ten").asText());
            result.put("majorClass", root.path("LopSV").asText());
            result.put("faculty", root.path("Khoa").asText());
            result.put("course", root.path("KhoaHoc").asText());
            result.put("dateOfBirth", root.path("NgaySinh").asText());
            result.put("sexuality", root.path("GioiTinh").asText());
            result.put("ethnicGroup", root.path("DanToc").asText());
            result.put("hometown", root.path("QueQuan").asText());
            result.put("placeOfBirth", root.path("NoiSinh").asText());
            result.put("religion", root.path("TonGiao").asText());
            result.put("email", root.path("Email").asText());
            result.put("idCardNumber", root.path("SoCMND").asText());
            result.put("idIssuePlace", root.path("NoiCapCMND").asText());
            result.put("idIssueDate", root.path("NgayCapCMND").asText());
            result.put("phoneNumber", root.path("SDTDiDong").asText());

            return result;
        } catch (Exception ex) {
            throw new ApiCallException("Error why trying to get user info. Exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
