package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Nickname;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberUpdateRequest {
    // 기본 정보
    @Pattern(regexp = Nickname.REGEX, message = Nickname.ERR_MSG)
    private String nickname;
    private String profileImage;


}

