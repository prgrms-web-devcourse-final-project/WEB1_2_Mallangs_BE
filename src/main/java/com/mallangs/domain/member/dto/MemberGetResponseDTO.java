package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.UserId;
import java.time.LocalDate;

public class MemberGetResponseDTO {

    private UserId userId;
    private Nickname nickname;
    private Email email;
    private String profileImage;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    //주소
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
    private Double x;
    private Double y;

    //주소2
    private String addressName2;
    private String addressType2;
    private String region1depthName2;
    private String region2depthName2;
    private String region3depthName2;
    private String region3depthHName2;
    private String mainAddressNo2;
    private String subAddressNo2;
    private String roadName2;
    private String mainBuildingNo2;
    private String subBuildingNo2;
    private String buildingName2;
    private String zoneNo2;
    private String mountainYn2;
    private Double x2;
    private Double y2;
}
