package com.mallangs.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    //상대방이 나가도 채팅방 유지 되야하기에 Cascade, orphan X
    @OneToMany(mappedBy = "chatRoom")
    @Builder.Default
    private List<ParticipatedRoom> occupiedRooms = new ArrayList<>();

    @Column(name = "chat_room_name")
    private String chatRoomName;

    public void addParticipatedRoom (ParticipatedRoom participatedRoom) {
        occupiedRooms.add(participatedRoom);
        participatedRoom.changeChatRoom(this);
    }
    public void changeChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

}
