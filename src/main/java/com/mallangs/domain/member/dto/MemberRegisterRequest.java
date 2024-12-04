package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Member;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberRegisterRequest {
    private String message;
    private String userId;
    private String nickName;
    private String success;

    public MemberRegisterRequest(Member member) {
        this.message = "회원가입에 성공하였습니다";
        this.userId = member.getUserId().getValue();
        this.nickName = member.getNickname().getValue();
        this.success = "성공 201";
    }
}
