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
    private Long chatRoomId;
    private Integer changedIsRead;

    @Builder
    public ChatRoomResponse(String chatRoomName, String memberNickname, Long memberId, Integer changedIsRead, Long chatRoomId) {
        this.chatRoomName = chatRoomName;
        this.memberNickname = memberNickname;
        this.memberId = memberId;
        this.chatRoomId = chatRoomId;
        this.changedIsRead = changedIsRead;
    }
}
