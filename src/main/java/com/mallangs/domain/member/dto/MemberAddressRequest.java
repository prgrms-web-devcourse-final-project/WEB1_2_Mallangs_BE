package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.util.GeometryUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


@Getter
@ToString
public class MemberAddressRequest {
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
    @Min(-90)@Max(90)
    private Double longitude;
    @Min(-90)@Max(90)
    private Double latitude; //y


    public Address toEntity() {
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
}
