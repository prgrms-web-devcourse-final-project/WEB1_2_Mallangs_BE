package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberAddressResponse {

    private String addressName;
    private String region3depthName;
    private String mainAddressNo;
    private String roadName;
    private Double x;
    private Double y;

    public MemberAddressResponse(Address address){
        this.addressName = address.getAddressName();
        this.region3depthName = address.getRegion3depthName();
        this.mainAddressNo = address.getMainAddressNo();
        this.roadName = address.getRoadName();
        this.x = address.getX();
        this.y = address.getY();
    }
}
