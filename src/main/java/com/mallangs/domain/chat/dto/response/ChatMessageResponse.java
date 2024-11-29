package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.chat.entity.IsRead;
import com.mallangs.domain.chat.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String profileImage;
    private String imageUrl;
    private String sender;
    private String message;
    private MessageType type;
    private boolean isRead;

    @Builder
    public ChatMessageResponse(Long chatMessageId, Long chatRoomId, String profileImage, String sender, String message,String imageUrl, MessageType type, boolean isRead) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.profileImage = profileImage;
        this.sender = sender;
        this.message = message;
        this.imageUrl = imageUrl;
        this.type = type;
        this.isRead = isRead;
    }
}
