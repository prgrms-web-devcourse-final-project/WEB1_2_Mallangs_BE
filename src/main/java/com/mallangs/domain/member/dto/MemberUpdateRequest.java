package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MemberUpdateRequest {
    // 기본 정보
    @Pattern(regexp = UserId.REGEX, message = UserId.ERR_MSG)
    private String userId;
    @Pattern(regexp = Nickname.REGEX, message = Nickname.ERR_MSG)
    private String nickname;
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
    @Pattern(regexp = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|tiff|webp|svg|ico|heic|heif|avif))$)",
            message = "유효한 이미지 파일을 업로드해주세요. (jpg, jpeg, png, gif, bmp, tiff, webp, svg, ico, heic, heif, avif)")
    private String profileImage;
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
    private List<MemberAddressRequest> addresses;
}

