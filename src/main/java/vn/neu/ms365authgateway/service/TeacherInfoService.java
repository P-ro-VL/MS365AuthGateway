package vn.neu.ms365authgateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeacherInfoService extends AbstractUserInfoService {

    private static final String API_URL = "https://hrm.neu.edu.vn/Api/StaffApi/GetThongTinCanBoGiangVien?email={id}@neu.edu.vn";

    public TeacherInfoService(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String buildUrl(String userId) {
        return API_URL.replace("{id}", userId);
    }

    @Override
    protected JsonNode extractDataNode(JsonNode root) {
        return root.path("data");
    }

    @Override
    protected Map<String, Object> mapUserInfo(JsonNode root) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", root.path("Email").asText().replaceAll("@neu.edu.vn", ""));
        result.put("firstName", root.path("Ten").asText());
        result.put("lastName", root.path("Ho").asText());
        result.put("fullName", root.path("HoTen").asText());
        result.put("majorClass", root.path("ChucDanh").asText());
        result.put("faculty", root.path("DonVi").asText());
        result.put("course", "");
        result.put("dateOfBirth", root.path("NgaySinh").asText());
        result.put("sexuality", root.path("GioiTinh").asBoolean() ? "Nu" : "Nam");
        result.put("ethnicGroup", "");
        result.put("hometown", root.path("NoiOHienNay").asText());
        result.put("placeOfBirth", "");
        result.put("religion", "");
        result.put("email", root.path("Email").asText());
        result.put("idCardNumber", root.path("CMND").asText());
        result.put("idIssuePlace", "");
        result.put("idIssueDate", "");
        result.put("phoneNumber", root.path("DienThoai").asText());
        return result;
    }
}
