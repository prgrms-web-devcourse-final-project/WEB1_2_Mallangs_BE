package com.mallangs.domain.member.dto.request;

import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequest {
    @Pattern(regexp = UserId.REGEX, message = UserId.ERR_MSG)
    private String userId;
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
}
