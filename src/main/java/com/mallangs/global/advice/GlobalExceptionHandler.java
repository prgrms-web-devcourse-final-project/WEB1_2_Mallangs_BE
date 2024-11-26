package com.mallangs.global.advice;

import com.mallangs.global.exception.ErrorResponse;
import com.mallangs.global.exception.MallangsCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MallangsCustomException.class)
  public ResponseEntity<ErrorResponse> handleArgsException(MallangsCustomException e) {
    ErrorResponse response = ErrorResponse.from(e.getErrorCode().getHttpStatus(), e.getMessage());
    return ResponseEntity.status(response.getHttpStatus())
        .body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleProductTaskException(HttpMessageNotReadableException e) {
    Map<String, String> map = new HashMap<>();
    map.put("error", "[JSON]" + " 형식을 확인해주세요");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleProductTaskException(MethodArgumentNotValidException e) {
    Map<String, String> map = new HashMap<>();
    map.put("error", e.getFieldError().getDefaultMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Map<String, String>> handleProductTaskException(NoResourceFoundException e) {
    Map<String, String> map = new HashMap<>();
    map.put("error", "URL을 잘못 입력하였습니다.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleProductTaskException(MethodArgumentTypeMismatchException e) {
    Map<String, String> map = new HashMap<>();
    map.put("error", "입력값 형식을 확인해주세요.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
    Map<String, String> map = Map.of("error", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
  }

}
