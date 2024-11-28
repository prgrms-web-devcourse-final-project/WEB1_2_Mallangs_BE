package com.mallangs.domain.chat.dto;

import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ParticipatedRoomListResponse {
    private Long participatedRoomId;
    private Long chatRoomId;
    private String nickname;
    private String message;
    private LocalDateTime lastChatTime;

    @Builder
    public ParticipatedRoomListResponse(String message, Nickname nickname, LocalDateTime createdAt, Long chatRoomId, Long participatedRoomId) {
        this.message = message;
        this.nickname = nickname.getValue();
        this.lastChatTime = createdAt;
        this.chatRoomId = chatRoomId;
        this.participatedRoomId = participatedRoomId;
    }

}