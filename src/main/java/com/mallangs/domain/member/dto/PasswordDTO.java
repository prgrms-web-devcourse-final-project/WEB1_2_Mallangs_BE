package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Password;
import lombok.Getter;
import lombok.ToString;
import jakarta.validation.constraints.Pattern;

@ToString
@Getter
public class PasswordDTO {
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
}
