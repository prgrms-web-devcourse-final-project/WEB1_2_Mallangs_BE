package com.mallangs.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@Entity
@ToString(exclude = "member")
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

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

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(name = "point", nullable = false, columnDefinition = "POINT SRID 4326")
    private Point point;

    public void addMember(Member member) {
        this.member = member;
    }
}
