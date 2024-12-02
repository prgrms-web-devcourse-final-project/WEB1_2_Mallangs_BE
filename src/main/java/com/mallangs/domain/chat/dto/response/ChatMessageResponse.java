package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.chat.entity.IsRead;
import com.mallangs.domain.chat.entity.MessageType;
import com.mallangs.domain.image.dto.ImageResponse;
import com.mallangs.domain.image.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChatMessageResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private String profileImage;
    private ImageResponse chatMessageImage;
    private String sender;
    private String message;
    private MessageType type;
    private LocalDateTime createTime;

    @Builder
    public ChatMessageResponse(Long chatMessageId, Long chatRoomId, String profileImage,
                               String sender, String message,ImageResponse chatMessageImage,
                               MessageType type, LocalDateTime createTime) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.profileImage = profileImage;
        this.sender = sender;
        this.message = message;
        this.chatMessageImage = chatMessageImage;
        this.type = type;
        this.createTime = createTime;
    }
}
