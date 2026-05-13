package vn.neu.ms365authgateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import vn.neu.ms365authgateway.api.ApiCallException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public abstract class AbstractUserInfoService {

    private final ObjectMapper objectMapper;

    protected AbstractUserInfoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> getUserInfo(String userId) throws ApiCallException {
        try {
            HttpURLConnection connection = openConnection(buildUrl(userId));

            try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = input.readLine()) != null) {
                    responseBody.append(line);
                }

                JsonNode root = objectMapper.readTree(responseBody.toString());
                return mapUserInfo(extractDataNode(root));
            } finally {
                connection.disconnect();
            }
        } catch (Exception ex) {
            throw new ApiCallException(
                    "Error why trying to get user info. Exception: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    protected JsonNode extractDataNode(JsonNode root) {
        return root;
    }

    protected abstract String buildUrl(String userId);

    protected abstract Map<String, Object> mapUserInfo(JsonNode root);

    private HttpURLConnection openConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        return connection;
    }
}
