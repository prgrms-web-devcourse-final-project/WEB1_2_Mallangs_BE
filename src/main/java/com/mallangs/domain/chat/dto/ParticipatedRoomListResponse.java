package com.mallangs.domain.chat.dto;

import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ParticipatedRoomListResponse {
    private List<ChatMessage> messages;
    private String nickname;
    private LocalDateTime createdAt;
    private Long chatRoomId;
    private Long participatedRoomId;

    public ParticipatedRoomListResponse(List<ChatMessage> messages, Nickname nickname, LocalDateTime createdAt, Long chatRoomId, Long participatedRoomId) {
        this.messages = messages;
        this.nickname = nickname.getValue();
        this.createdAt = createdAt;
        this.chatRoomId = chatRoomId;
        this.participatedRoomId = participatedRoomId;
    }

}