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
    @Pattern(regexp = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|tiff|webp|svg|ico|heic|heif|avif))$)",
            message = "유효한 이미지 파일을 업로드해주세요. (jpg, jpeg, png, gif, bmp, tiff, webp, svg, ico, heic, heif, avif)")
    private String profileImage;
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
    private Double latitude;
    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public Member toEntityMember() {
        return Member.builder()
                .userId(new UserId(userId))
                .nickname(new Nickname(nickname))
                .email(new Email(email))
                .profileImage(profileImage)
                .hasPet(hasPet).build();
    }
    public Address toEntityAddress() {
        //Point point = GeometryUtil.createPoint(latitude, longitude);
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
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
}
