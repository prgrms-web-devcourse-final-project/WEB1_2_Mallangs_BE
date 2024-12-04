package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ParticipatedRoomListResponse {
    private Long participatedRoomId;
    private Long chatRoomId;
    private String nickname;
    private String message;
    private String chatRoomName;
    private LocalDateTime lastChatTime;
    private Integer notReadCnt;

    @Builder
    public ParticipatedRoomListResponse(Long participatedRoomId, Long chatRoomId,
                                        String nickname, String message,
                                        String chatRoomName, LocalDateTime lastChatTime,
                                        Integer notReadCnt) {
        this.participatedRoomId = participatedRoomId;
        this.chatRoomId = chatRoomId;
        this.nickname = nickname;
        this.message = message;
        this.chatRoomName = chatRoomName;
        this.lastChatTime = lastChatTime;
        this.notReadCnt = notReadCnt;
    }
}