package vn.neu.ms365authgateway.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.neu.ms365authgateway.api.ApiCallException;
import vn.neu.ms365authgateway.api.ApiCallResult;
import vn.neu.ms365authgateway.api.ApiExecutorService;
import vn.neu.ms365authgateway.api.ApiResponse;
import vn.neu.ms365authgateway.dto.request.AuthQRRequest;
import vn.neu.ms365authgateway.dto.request.AuthRequest;
import vn.neu.ms365authgateway.dto.response.AuthResponse;
import vn.neu.ms365authgateway.service.MSAuthService;
import vn.neu.ms365authgateway.service.QRCodeAuthService;

import java.util.Map;

@RestController
@RequestMapping(path = "/v1/auth")
@AllArgsConstructor
public class AuthEndpoint {

    ApiExecutorService apiExecutorService;

    MSAuthService msAuthService;
    QRCodeAuthService qrCodeAuthService;

    @PostMapping(path = "/ms")
    public ResponseEntity<ApiResponse<AuthResponse>> authMS365(@RequestBody AuthRequest request, HttpServletRequest httpServletRequest) throws ApiCallException {
        return apiExecutorService.execute(httpServletRequest, () -> new ApiCallResult<>(msAuthService.auth(request)));
    }

    @PostMapping(path = "/code")
    public ResponseEntity<ApiResponse<Map<String, Object>>> authQRCode(@RequestBody AuthQRRequest request, HttpServletRequest httpServletRequest) throws ApiCallException {
        return apiExecutorService.execute(httpServletRequest, () -> new ApiCallResult<>(qrCodeAuthService.getStudentInfo(request.getCode())));
    }

}
