package com.mallangs.domain.member.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberGetRequestByEmail {
    //관리자 회원 조회 dto
    private int page;
    private int size;
    private int days;
    private Boolean isActive;
    private String email;
}
