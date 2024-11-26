package com.mallangs.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // Member
  MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
  FAILURE_REQUEST(BAD_REQUEST,"잘못된 요청입니다."),
  UNMATCHED_PASSWORD(BAD_REQUEST,"입력한 정보가 일치하지 않습니다."),
  BANNED_MEMBER(NOT_ACCEPTABLE,"차단된 계정입니다."),

  // Address
  ADDRESS_NOT_FOUND(NOT_FOUND, "주소 정보를 찾을 수 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;

}
