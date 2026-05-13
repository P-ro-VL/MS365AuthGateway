package vn.neu.ms365authgateway.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.neu.ms365authgateway.api.ApiCallException;
import vn.neu.ms365authgateway.api.ApiCallResult;
import vn.neu.ms365authgateway.api.ApiExecutorService;
import vn.neu.ms365authgateway.api.ApiResponse;
import vn.neu.ms365authgateway.service.StudentInfoService;
import vn.neu.ms365authgateway.service.TeacherInfoService;

import java.util.Map;

@RestController
@RequestMapping(path = "/v1/user")
@AllArgsConstructor
public class UserInfoEndpoint {

    ApiExecutorService apiExecutorService;

    StudentInfoService studentInfoService;
    TeacherInfoService teacherInfoService;

    @GetMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> authMS365(@PathVariable String userId, @RequestParam boolean isStudent, HttpServletRequest httpServletRequest) throws ApiCallException {
        return apiExecutorService.execute(httpServletRequest, () -> new ApiCallResult<>(
                isStudent ? studentInfoService.getUserInfo(userId)
                        : teacherInfoService.getUserInfo(userId)
        ));
    }

}
