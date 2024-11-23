package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import jakarta.validation.constraints.Pattern;

public class MemberLoginRequestDTO {
    @Pattern(regexp = UserId.REGEX, message = UserId.ERR_MSG)
    private String userId;
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
}
