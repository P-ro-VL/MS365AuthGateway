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

import java.util.Map;

@RestController
@RequestMapping(path = "/v1/user")
@AllArgsConstructor
public class UserInfoEndpoint {

    ApiExecutorService apiExecutorService;

    StudentInfoService studentInfoService;

    @GetMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> authMS365(@PathVariable String userId, HttpServletRequest httpServletRequest) throws ApiCallException {
        return apiExecutorService.execute(httpServletRequest, () -> new ApiCallResult<>(studentInfoService.getUserInfo(userId)));
    }

}
