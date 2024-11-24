package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberCreateRequest {
    // 회원정보
    @Pattern(regexp = UserId.REGEX, message = UserId.ERR_MSG)
    private String userId;
    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
    @Pattern(regexp = Nickname.REGEX, message = Nickname.ERR_MSG)
    private String nickname;
    @Pattern(regexp = Email.REGEX, message = Email.ERR_MSG)
    private String email;
    @NotNull(message = "반려동물 유무는 필수 입력입니다.")
    private Boolean hasPet;

    // 주소
    @NotNull(message = "주소 이름은 필수입니다.")
    private String addressName;
    @NotNull(message = "주소 유형은 필수입니다.")
    private String addressType;
    @NotNull(message = "시는 필수입니다.")
    private String region1depthName;
    @NotNull(message = "군은 필수입니다.")
    private String region2depthName;
    @NotNull(message = "읍/면/리는 필수입니다.")
    private String region3depthName;
    @NotNull(message = "행정이름은 필수입니다.")
    private String region3depthHName;
    @NotNull(message = "주소 번호는 필수입니다.")
    private String mainAddressNo;
    @NotNull(message = "보조 주소 번호는 필수입니다.")
    private String subAddressNo;
    @NotNull(message = "도로명은 필수입니다.")
    private String roadName;
    @NotNull(message = "주 건물번호는 필수입니다.")
    private String mainBuildingNo;
    @NotNull(message = "보조 건물번호는 필수입니다.")
    private String subBuildingNo;
    @NotNull(message = "건물명은 필수입니다.")
    private String buildingName;
    @NotNull(message = "우편번호는 필수입니다.")
    private String zoneNo;
    @NotNull(message = "산악지역은 필수입력 값 입니다.")
    private String mountainYn;
    @NotNull(message = "위도는 필수입니다.")
    private Double x;
    @NotNull(message = "경도는 필수입니다.")
    private Double y;

}
