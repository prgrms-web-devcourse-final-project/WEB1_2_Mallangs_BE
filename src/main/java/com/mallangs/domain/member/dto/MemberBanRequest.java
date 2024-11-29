package com.mallangs.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MemberBanRequest {
    @NotNull(message = "회원 번호는 필수입니다.")
    private List<Long> memberIds;
    @NotNull(message = "7, 15,30일 중 1개를 입력해주세요.")
    private Integer days;
    @NotNull(message = "차단 사유는 필수입니다.")
    private String reason;
}
