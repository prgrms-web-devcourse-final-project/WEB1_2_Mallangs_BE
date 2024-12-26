package com.mallangs.domain.chat.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomDeleteResponse {
    private String success;
    private String message;
    private Long participatedRoomId;

    public ChatRoomDeleteResponse(Long participatedRoomId) {
        this.success = "성공";
        this.message = "참여채팅방 삭제에 성공하였습니다.";
        this.participatedRoomId = participatedRoomId;
    }
}
