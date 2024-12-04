package com.mallangs.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mallangs.domain.image.entity.Image;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member sender;

    @JoinColumn(name = "message")
    private String message;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType type = MessageType.ENTER;

    @Builder.Default
    private Boolean senderRead = false;

    @Builder.Default
    private Boolean receiverRead = false;

    public void changeMessage(String message) {
        this.message = message;
    }

}
