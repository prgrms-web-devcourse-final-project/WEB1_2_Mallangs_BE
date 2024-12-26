package com.mallangs.domain.chat.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomCreateResponse {
    private String success;
    private String message;
    private Long chatRoomId;

    public ChatRoomCreateResponse(Long chatRoomId) {
        this.success = "성공";
        this.message = "채팅 생성에 성공하였습니다.";
        this.chatRoomId = chatRoomId;
    }
}
