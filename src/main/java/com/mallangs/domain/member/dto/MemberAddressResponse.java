package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberAddressResponse {

    private Long addressId;
    private String addressName;
    private String region3depthName;
    private String mainAddressNo;
    private String roadName;
    private Double x;
    private Double y;

    public MemberAddressResponse(Address address){
        this.addressId = address.getId();
        this.addressName = address.getAddressName();
        this.region3depthName = address.getRegion3depthName();
        this.mainAddressNo = address.getMainAddressNo();
        this.roadName = address.getRoadName();
        this.x = address.getPoint().getX();
        this.y = address.getPoint().getY();
    }
}