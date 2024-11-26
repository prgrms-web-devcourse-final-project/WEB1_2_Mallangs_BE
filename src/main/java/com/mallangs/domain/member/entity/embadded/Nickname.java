package com.mallangs.domain.member.entity.embadded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {
    public static final String REGEX = "^[a-zA-Z0-9가-힣]{2,15}$\n";
    public static final String ERR_MSG = "닉네임은 특수문자를 제외한 2~15자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "nickname", nullable = false, length = 50)
    private String value;

    public Nickname(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
