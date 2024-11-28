package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberGetResponseOnlyMember {
    //관리자 회원리스트 조회시 사용
    private Long memberId;
    private String userId;
    private String nickname;
    private String email;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private Boolean isActive;

    public MemberGetResponseOnlyMember(Member member) {
        this.memberId = member.getMemberId();
        this.userId = member.getUserId().getValue();
        this.nickname = member.getNickname().getValue();
        this.email = member.getEmail().getValue();
        this.lastLoginTime = member.getLastLoginTime();
        this.createdAt = member.getCreatedAt();
        this.isActive = member.getIsActive();
    }
}
