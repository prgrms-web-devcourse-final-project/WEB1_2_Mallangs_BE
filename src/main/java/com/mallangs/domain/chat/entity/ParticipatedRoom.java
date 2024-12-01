package com.mallangs.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mallangs.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "participated_room")
public class ParticipatedRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participated_room_id")
    private Long participatedRoomId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member participant;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "participatedRoom", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    private Long lastReadMessageId;

    public void addChatMessage(ChatMessage chatMessage) {
        messages.add(chatMessage);
    }
    public void removeChatMessage(ChatMessage chatMessage) {
        messages.remove(chatMessage);
    }
    public void changeChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    public void changeParticipant(Member member) {
        this.participant = member;
    }
}
