package com.mallangs.global.advice;

import com.mallangs.global.exception.ErrorResponse;
import com.mallangs.global.exception.MallangsCustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MallangsCustomException.class)
  public ResponseEntity<ErrorResponse> handleArgsException(MallangsCustomException e) {
    ErrorResponse response = ErrorResponse.from(e.getErrorCode().getHttpStatus(), e.getMessage());
    return ResponseEntity.status(response.getHttpStatus())
        .body(response);
  }

}
