package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.util.GeometryUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


@Getter
@ToString
@NoArgsConstructor
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
    private String addressName;
    private String addressType;
    private String region1depthName;
    private String region2depthName;
    private String region3depthName;
    private String region3depthHName;
    private String mainAddressNo;
    private String subAddressNo;
    private String roadName;
    private String mainBuildingNo;
    private String subBuildingNo;
    private String buildingName;
    private String zoneNo;
    private String mountainYn;
    private Double latitude;
    private Double longitude;


    public Member toEntityMember() {
        return Member.builder()
                .userId(new UserId(userId))
                .nickname(new Nickname(nickname))
                .email(new Email(email))
                .hasPet(hasPet).build();
    }
    public Address toEntityAddress() {
        Point point = GeometryUtil.createPoint(latitude, longitude);
        return Address.builder()
                .addressName(addressName)
                .addressType(addressType)
                .region1depthName(region1depthName)
                .region2depthName(region2depthName)
                .region3depthName(region3depthName)
                .region3depthHName(region3depthHName)
                .mainAddressNo(mainAddressNo)
                .subAddressNo(subAddressNo)
                .roadName(roadName)
                .mainBuildingNo(mainBuildingNo)
                .subBuildingNo(subBuildingNo)
                .buildingName(buildingName)
                .zoneNo(zoneNo)
                .mountainYn(mountainYn)
                .point(point)
                .build();
    }
    @Builder
    public MemberCreateRequest(String userId, String password, String nickname, String email, Boolean hasPet) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.hasPet = hasPet;
    }
}
