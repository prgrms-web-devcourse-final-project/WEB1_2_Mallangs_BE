package com.mallangs.global.exception;

import lombok.Getter;

@Getter
public class MallangsCustomException extends RuntimeException {

  private final ErrorCode errorCode;

  public MallangsCustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
