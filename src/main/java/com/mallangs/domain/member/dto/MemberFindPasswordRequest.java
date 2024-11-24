package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.UserId;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberFindPasswordRequest {
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
    @Pattern(regexp = UserId.REGEX, message = UserId.ERR_MSG)
    private String userId;
}
