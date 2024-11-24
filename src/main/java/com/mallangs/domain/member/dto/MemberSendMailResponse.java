package com.mallangs.domain.member.dto;

import lombok.Data;

@Data
public class MemberSendMailResponse {
    private String address;
    private String title;
    private String message;
}
