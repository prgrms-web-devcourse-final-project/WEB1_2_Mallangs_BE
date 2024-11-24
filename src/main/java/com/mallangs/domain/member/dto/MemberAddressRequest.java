package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberAddressRequest {
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

    public Address toEntity() {
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
                .x(x)
                .y(y)
                .build();
    }
}
