package com.mallangs.domain.chat.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
public class UpdateChatMessageRequest {

    private Long chatMessageId;
    @Length(min = 1, max = 300)
    private String message;
}
