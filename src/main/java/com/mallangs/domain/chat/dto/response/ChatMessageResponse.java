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
    private String chatMessageImage;
    private String sender;
    private String message;
    private MessageType type;
    private LocalDateTime createTime;

    @Builder
    public ChatMessageResponse(Long chatMessageId, Long chatRoomId,
                               String sender, String message,String chatMessageImage,
                               MessageType type, LocalDateTime createTime) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.message = message;
        this.chatMessageImage = chatMessageImage;
        this.type = type;
        this.createTime = createTime;
    }
}
