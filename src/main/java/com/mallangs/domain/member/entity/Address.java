package com.mallangs.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "address_type", nullable = false)
    private String addressType;

    @Column(name = "region_1depth_name")
    private String region1depthName;

    @Column(name = "region_2depth_name")
    private String region2depthName;

    @Column(name = "region_3depth_name")
    private String region3depthName;

    @Column(name = "region_3depth_h_name")
    private String region3depthHName;

    @Column(name = "main_address_no")
    private String mainAddressNo;

    @Column(name = "sub_address_no")
    private String subAddressNo;

    @Column(name = "road_name")
    private String roadName;

    @Column(name = "main_building_no")
    private String mainBuildingNo;

    @Column(name = "sub_building_no")
    private String subBuildingNo;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "zone_no")
    private String zoneNo;

    @Column(name = "mountain_yn")
    private String mountainYn; // Y / N 으로 입력됨

    @Column(name = "x", nullable = false)
    private Double x;

    @Column(name = "y", nullable = false)
    private Double y;
}
