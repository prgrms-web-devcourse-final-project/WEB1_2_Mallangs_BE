package com.mallangs.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSendMailResponse {
    private String email;
    private String title;
    private String message;
}
