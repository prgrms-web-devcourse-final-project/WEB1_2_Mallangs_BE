package com.mallangs.domain.chat.dto.response;

import com.mallangs.domain.chat.entity.IsRead;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class IsReadResponse {

    private String sender;
    private Boolean readCheck;

    public IsReadResponse(IsRead isRead) {
        this.sender = isRead.getSender();
        this.readCheck = isRead.getReadCheck();
    }
}
