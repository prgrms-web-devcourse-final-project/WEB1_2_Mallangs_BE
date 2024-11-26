package com.mallangs.domain.member.entity.embadded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Pattern;

@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {
    public static final String REGEX = "^[a-zA-Z0-9]{6,12}$\n";
    public static final String ERR_MSG = "아이디는 영문 대소문자, 숫자만 포함한 6~12자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "user_id", nullable = false, length = 20)
    private String value;

    public UserId(final String userId) {
        if (!PATTERN.matcher(userId).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = userId;
    }
}