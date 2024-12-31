package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.chat.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChatMessageResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private String message;
    private String base64Image;
    private MessageType type;
    private LocalDateTime createTime;

    @Builder
    public ChatMessageResponse(Long chatMessageId, Long chatRoomId,
                               String sender, String message, MessageType type, LocalDateTime createTime, String base64Image) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.base64Image = base64Image;
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.createTime = createTime;
    }
}
