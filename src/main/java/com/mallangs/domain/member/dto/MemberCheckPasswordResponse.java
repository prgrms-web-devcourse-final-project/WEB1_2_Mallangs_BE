package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberCheckPasswordResponse {
    private String message;
    private String success;
    private String userId;

    public MemberCheckPasswordResponse(Member member) {
        this.message = "비밀번호 찾기에 성공하였습니다.";
        this.success = "비밀번호 찾기 성공";
        this.userId = member.getUserId().getValue();
    }

}
