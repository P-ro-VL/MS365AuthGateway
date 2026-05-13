package vn.neu.ms365authgateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentInfoService extends AbstractUserInfoService {

    private static final String API_URL = "https://api.neu.edu.vn/api/StudentInfo/";

    public StudentInfoService(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String buildUrl(String userId) {
        return API_URL + userId;
    }

    @Override
    protected Map<String, Object> mapUserInfo(JsonNode root) {
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
    }
}
