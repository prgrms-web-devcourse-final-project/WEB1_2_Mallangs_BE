package com.mallangs.domain.chat.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatRoomChangeNameRequest {

    private Long chatRoomId;
    private String roomName;
}
