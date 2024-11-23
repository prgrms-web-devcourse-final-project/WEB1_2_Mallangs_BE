package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import jakarta.validation.constraints.Pattern;

public class MemberFindPasswordRequestDTO {
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
}
