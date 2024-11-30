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
    @JoinColumn(name = "participated_room_id", nullable = false)
    private ParticipatedRoom participatedRoom;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member sender;

    @JoinColumn(name = "message")
    private String message;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Image messageImage;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType type = MessageType.ENTER;

    @Builder.Default
    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IsRead> isRead = new ArrayList<>();

    public void changeMessage(String message) {
        this.message = message;
    }
    public void addIsRead(IsRead isRead) {
        this.isRead.add(isRead);
        isRead.changeChatMessage(this);
    }
    public void addImage(Image image) {
        this.messageImage = image;
    }

}
