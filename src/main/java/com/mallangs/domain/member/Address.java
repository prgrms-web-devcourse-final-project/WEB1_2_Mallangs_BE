package com.mallangs.domain.member;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "address_name", nullable = false)
    private String addressName; // 주소 이름

    @Column(name = "city", nullable = false)
    private String city; // 시/도

    @Column(name = "town", nullable = false)
    private String town; // 시/군/구

    @Column(name = "region_3depth_name")
    private String village; // 읍/면/동

    @Column(name = "longitude", nullable = false)
    private String longitude; // 경도

    @Column(name = "latitude", nullable = false)
    private String latitude; // 위도

    @Column(name = "detail_info")
    private String detailInfo; // 세부주소

}