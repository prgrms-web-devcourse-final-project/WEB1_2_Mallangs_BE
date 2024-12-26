package com.mallangs.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomResponse {
    private String chatRoomName;
    private String memberNickname;
    private Long memberId;
    private Integer changedIsRead;

    @Builder
    public ChatRoomResponse(String chatRoomName, String memberNickname, Long memberId, Integer changedIsRead) {
        this.chatRoomName = chatRoomName;
        this.memberNickname = memberNickname;
        this.memberId = memberId;
        this.changedIsRead = changedIsRead;
    }
}
