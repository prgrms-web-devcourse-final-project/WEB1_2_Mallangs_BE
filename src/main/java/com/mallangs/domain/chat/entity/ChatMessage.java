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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messages", nullable = false)
    private ParticipatedRoom participatedRoom;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType type;


}
