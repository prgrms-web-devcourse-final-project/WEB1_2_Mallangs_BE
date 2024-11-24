package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberFindIdRequest {
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
    @Pattern(regexp = Nickname.REGEX, message = Nickname.ERR_MSG)
    private String nickname;
}
