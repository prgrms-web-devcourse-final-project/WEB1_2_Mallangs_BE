package com.mallangs.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // Member
  MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
  FAILURE_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),
  FAILURE(INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
  UNMATCHED_PASSWORD(BAD_REQUEST, "입력한 정보가 일치하지 않습니다."),
  BANNED_MEMBER(NOT_ACCEPTABLE, "차단된 계정입니다."),
  NOT_FOUND_PROFILE_IMAGE(NOT_FOUND, "이미지 파일이 존재하지 않습니다."),
  UNSUPPORTED_FILE_TYPE(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
  FAILED_BLACKLIST_TOKEN(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
  DUPLICATE_USER_ID(BAD_REQUEST, "중복 아이디입니다."),
  DUPLICATE_EMAIL(BAD_REQUEST, "중복 이메일입니다."),
  DATABASE_ERROR(INTERNAL_SERVER_ERROR, "DB 저장에 문제가 발생했습니다."),

  // Address
  ADDRESS_NOT_FOUND(NOT_FOUND, "주소 정보를 찾을 수 없습니다."),

  // ChatRoom
  CHATROOM_NOT_FOUND(NOT_FOUND, "채팅방 정보를 찾을 수 없습니다"),
  FAILED_DELETE_CHATROOM(UNAUTHORIZED, "참여 채팅방 삭제 권한이 없는 사용자 입니다."),
  PARTICIPATED_ROOM_NOT_FOUND(NOT_FOUND, "참여채팅방 정보를 찾을 수 없습니다."),
  FAILED_UPDATE_CHAT_ROOM(NOT_FOUND, "참여채팅방 정보를 찾을 수 없습니다."),

  //ChatMessage
  FAILED_GET_CHAT_MESSAGE(BAD_REQUEST, "채팅 조회에 실패하였습니다."),
  FAILED_DELETE_CHAT_MESSAGE(INTERNAL_SERVER_ERROR, "채팅 삭제에 실패하였습니다."),
  FAILED_CREATE_CHAT_MESSAGE(INTERNAL_SERVER_ERROR, "채팅 등록에 실패하였습니다."),
  CHAT_MESSAGE_NOT_FOUND(NOT_FOUND, "채팅 정보를 찾을 수 없습니다."),

  // Article
  ARTICLE_TYPE_NOT_FOUND(NOT_FOUND, "해당하는 글타래 타입을 찾을 수 없습니다."),
  ARTICLE_NOT_FOUND(NOT_FOUND, "글타래 정보를 찾을 수 없습니다."),
  ARTICLE_NOT_VISIBLE(FORBIDDEN, "글타래 정보를 볼 권한이 없습니다."),
  UNAUTHORIZED_MODIFY(FORBIDDEN, "이 리소스를 수정할 권한이 없습니다."),
  UNAUTHORIZED_DELETE(FORBIDDEN, "이 리소스를 삭제할 권한이 없습니다."),
  RESOURCE_NOT_MODIFIABLE(FORBIDDEN, "이 리소스는 수정할 수 없습니다."),
  RESOURCE_NOT_DELETABLE(FORBIDDEN, "이 리소스는 삭제할 수 없습니다."),
  INVALID_TYPE_CHANGE(BAD_REQUEST, "글타래 타입은 생성 시에 설정된 타입을 유지해야 합니다."),

  // Board
  CATEGORY_NOT_FOUND(NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),
  PARENT_CATEGORY_NOT_FOUND(NOT_FOUND, "부모 카테고리를 찾을 수 없습니다."),
  BOARD_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다."),
  UNAUTHORIZED_ACCESS(FORBIDDEN, "관리자 권한이 필요합니다."),
  INVALID_BOARD_TYPE(BAD_REQUEST, "잘못된 게시판 타입입니다."),
  INVALID_BOARD_STATUS(BAD_REQUEST, "게시글을 볼 수 없는 상태입니다."),

  //Review
  REVIEW_NOT_FOUND(NOT_FOUND, "리뷰 정보를 찾을 수 없습니다."),
  REVIEW_NOT_OWNED(FORBIDDEN, "해당 리뷰에 권한이 없습니다"),
  REVIEW_NOT_OPEN(FORBIDDEN, "해당 리뷰가 숨김 상태 입니다."),
  REVIEW_NOT_CREATED(INTERNAL_SERVER_ERROR, "리뷰 등록에 실패하였습니다"),
  REVIEW_NOT_UPDATED(INTERNAL_SERVER_ERROR, "리뷰 수정에 실패하였습니다"),
  REVIEW_NOT_DELETE(INTERNAL_SERVER_ERROR, "리뷰 삭제에 실패하였습니다"),

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
  LOCATION_INVALIDE_RANGE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 범위입니다."),

  // Comment
  COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다."),
  COMMENT_NOT_REGISTERED(CONFLICT, "댓글 등록에 실패했습니다."),
  COMMENT_NOT_MODIFIED(CONFLICT, "댓글 업데이트에 실패했습니다."),
  COMMENT_NOT_REMOVED(CONFLICT, "댓글 삭제에 실패했습니다."),
  FORBIDDEN_ACCESS(FORBIDDEN, "권한이 없습니다."),
  POST_NOT_FOUND(NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),

  // Image
  IMAGE_COUNT_EXCEEDED(BAD_REQUEST, "이미지 개수가 혀용치(4개)를 초과했습니다."),
  IMAGE_SIZE_EXCEEDED(BAD_REQUEST, "이미지 크기가 허용치(3MB)를 초과했습니다."),
  IMAGE_NOT_FOUND(NOT_FOUND, "이미지를 찾을 수 없습니다."),
  INVALID_IMAGE_FORMAT(BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
  IMAGE_FILE_IS_REQUIRED(BAD_REQUEST, "파일이 없습니다."),
  IMAGE_UPLOAD_FAILED(BAD_REQUEST, "업로드에 실패했습니다."),
  IMAGE_PROCESSING_ERROR(BAD_REQUEST,"이미지 처리 중 오류가 발생했습니다.");

  private final HttpStatus httpStatus;
  private final String message;

}
