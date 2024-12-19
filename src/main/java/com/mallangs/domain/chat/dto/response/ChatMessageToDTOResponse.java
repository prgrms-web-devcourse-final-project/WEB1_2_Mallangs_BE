package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChatMessageToDTOResponse {

    private Long chatMessageId;
    private Long senderId;
    private String senderName;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessageToDTOResponse(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getChatMessageId();
        this.senderId = chatMessage.getSender().getMemberId();
        this.senderName = chatMessage.getSender().getNickname().getValue();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
