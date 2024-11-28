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
  NOT_FOUND_PROFILE_IMAGE(NOT_FOUND, "이미지 파일이 존재하지 않습니다."),
  UNSUPPORTED_FILE_TYPE(NOT_FOUND, "지원하지 않는 파일 형식입니다."),

  // Address
  ADDRESS_NOT_FOUND(NOT_FOUND, "주소 정보를 찾을 수 없습니다."),

  // ChatRoom
  CHATROOM_NOT_FOUND(NOT_FOUND, "채팅방 정보를 찾을 수 없습니다"),
  FAILED_DELETE_CHATROOM(UNAUTHORIZED, "참여 채팅방 삭제 권한이 없는 사용자 입니다."),
  PARTICIPATED_ROOM_NOT_FOUND(NOT_FOUND,"참여채팅방 정보를 찾을 수 없습니다."),

  //ChatMessage
  FAILED_GET_CHAT_MESSAGE(BAD_REQUEST, "채팅 조회에 실패하였습니다."),
  FAILED_DELETE_CHAT_MESSAGE(INTERNAL_SERVER_ERROR, "채팅 삭제에 실패하였습니다."),
  FAILED_CREATE_CHAT_MESSAGE(INTERNAL_SERVER_ERROR, "채팅 등록에 실패하였습니다."),
  CHAT_MESSAGE_NOT_FOUND(NOT_FOUND,"채팅 정보를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

}
