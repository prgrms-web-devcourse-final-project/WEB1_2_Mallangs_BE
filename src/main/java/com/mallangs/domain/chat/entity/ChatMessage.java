package com.mallangs.domain.chat.entity;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participated_room_id", nullable = false)
    private ParticipatedRoom participatedRoom;

    private String sender;

    @JoinColumn(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType type = MessageType.ENTER;

    @Builder.Default
    private Boolean isRead = false;

    public void changeParticipatedRoom(ParticipatedRoom participatedRoom){
        this.participatedRoom = participatedRoom;
    }

}
