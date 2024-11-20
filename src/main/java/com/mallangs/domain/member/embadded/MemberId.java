package com.mallangs.domain.member.embadded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberId {
    public static final String REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,12}$\n";
    public static final String ERR_MSG = "아이디는 영문 대소문자, 숫자를 포함한 6~12자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "member_id", nullable = false, length = 12)
    private String value;

    public MemberId(final String memberId) {
        if (!PATTERN.matcher(memberId).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = memberId;
    }
}