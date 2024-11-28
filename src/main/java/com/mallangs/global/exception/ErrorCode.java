package com.mallangs.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
  BOARD_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다."),
  UNAUTHORIZED_BOARD_ACCESS(FORBIDDEN, "게시글에 대한 권한이 없습니다."),
  INVALID_BOARD_TYPE(BAD_REQUEST, "잘못된 게시판 타입입니다."),
  INVALID_BOARD_STATUS(BAD_REQUEST, "게시글을 볼 수 없는 상태입니다."),
  //PET
  PET_NOT_FOUND(NOT_FOUND, "반려동물 정보를 찾을 수 없습니다"),
  PET_NOT_ACTIVATE(HttpStatus.GONE, "반려동물이 비활성화(삭제) 상태입니다."),
  PET_NOT_PROFILE_OPEN(HttpStatus.FORBIDDEN, "반려동물 비공개 상태입니다."),
  PET_NOT_CREATE(HttpStatus.BAD_REQUEST, "반려동물 등록에 실패하였습니다."),
  PET_NOT_UPDATE(HttpStatus.INTERNAL_SERVER_ERROR, "반려동물 수정에 실패하였습니다."),
  PET_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "반려동물 삭제(비활성화)에 실패하였습니다."),
  PET_NOT_RESTORE(HttpStatus.INTERNAL_SERVER_ERROR, "반려동물 복원(활성화)에 실패하였습니다."),
  PET_NOT_SEARCH_LOCATION(HttpStatus.INTERNAL_SERVER_ERROR, "주변 반려동물 검색에 실패하였습니다."),
  PET_NOT_OWNED(HttpStatus.FORBIDDEN, "반려동물에 대한 권한이 없습니다."),
  PET_NOT_REPRESENTATIVE(HttpStatus.INTERNAL_SERVER_ERROR, "대표 말랑이(반려동물) 등록에 실패하였습니다."),

  //Location
  LOCATION_INVALIDE_PARAMS(HttpStatus.INTERNAL_SERVER_ERROR, "입력된 데이터가 유효하지 않습니다."),
  LOCATION_INVALIDE_RANGE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 범위입니다.");

  private final HttpStatus httpStatus;
  private final String message;

}
