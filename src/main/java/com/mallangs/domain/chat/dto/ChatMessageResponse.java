package com.mallangs.domain.chat.dto;

import com.mallangs.domain.chat.entity.ParticipatedRoom;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageResponse {

    private Long chatMessageId;
    private ParticipatedRoom participatedRoom;
    private String sender;
    private String message;
    private Boolean isRead;

    public ChatMessageResponse(Long chatMessageId, ParticipatedRoom participatedRoom, String sender, String message, Boolean isRead) {
        this.chatMessageId = chatMessageId;
        this.participatedRoom = participatedRoom;
        this.sender = sender;
        this.message = message;
        this.isRead = isRead;
    }
}
