package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberUpdateRequest {
    // 기본 정보
    @Pattern(regexp = Nickname.REGEX, message = Nickname.ERR_MSG)
    private String nickname;
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
}

