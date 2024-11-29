package com.mallangs.domain.chat.entity;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participated_room_id", nullable = false)
    private ParticipatedRoom participatedRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member sender;

    @JoinColumn(name = "message")
    private String message;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType type = MessageType.ENTER;

    @Builder.Default
    private Boolean isRead = false;

    public void changeMessage(String message) {
        this.message = message;
    }

}
