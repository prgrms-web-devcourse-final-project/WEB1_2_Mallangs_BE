package com.mallangs.domain.chat.dto.response;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChatMessageSuccessResponse {
    private String success;
    private String message;
    private String userId;

    public ChatMessageSuccessResponse(String userId) {
        this.success = "성공";
        this.message = "채팅 보내기에 성공하였습니다.";
        this.userId = userId;
    }

}
