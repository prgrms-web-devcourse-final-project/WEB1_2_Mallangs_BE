package com.mallangs.domain.chat.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
public class ChatMessageRequest {

    private Long participatedRoomId;
    private Long memberId;
    @Length(min = 1, max = 300)
    private String message;
    private String base64Image;

}
