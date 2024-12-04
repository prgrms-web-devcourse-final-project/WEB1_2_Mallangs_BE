package com.mallangs.domain.chat.dto.response;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ChatMessageDeleteSuccessResponse {
    private String success;
    private String message;
    private Long chatMessageId;

    public ChatMessageDeleteSuccessResponse(Long chatMessageId) {
        this.success = "성공";
        this.message = "채팅 삭제에 성공하였습니다.";
        this.chatMessageId = chatMessageId;
    }
}
