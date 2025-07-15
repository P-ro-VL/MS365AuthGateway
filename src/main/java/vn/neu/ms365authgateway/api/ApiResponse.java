package vn.neu.ms365authgateway.api;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
  ApiMeta meta;
  T data;

  public ResponseEntity<ApiResponse<T>> toResponseEntity(HttpStatus status) {
      return new ResponseEntity<>(this, status);
  }
}