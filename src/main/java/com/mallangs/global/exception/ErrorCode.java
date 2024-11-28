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

  // Address
  ADDRESS_NOT_FOUND(NOT_FOUND, "주소 정보를 찾을 수 없습니다."),

  // Board
  CATEGORY_NOT_FOUND(NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),
  PARENT_CATEGORY_NOT_FOUND(NOT_FOUND, "부모 카테고리를 찾을 수 없습니다."),
  BOARD_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다."),
  UNAUTHORIZED_BOARD_ACCESS(FORBIDDEN, "게시글에 대한 권한이 없습니다."),
  INVALID_BOARD_TYPE(BAD_REQUEST, "잘못된 게시판 타입입니다."),
  INVALID_BOARD_STATUS(BAD_REQUEST, "게시글을 볼 수 없는 상태입니다."),;

  private final HttpStatus httpStatus;
  private final String message;

}
