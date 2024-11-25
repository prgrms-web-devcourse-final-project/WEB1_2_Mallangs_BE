package com.mallangs.domain.pet.dto;

import lombok.Getter;

@Getter
public class PetLocationRequest {
    private Double y;    // 위도 (y 좌표) latitude
    private Double x;   // 경도 (x 좌표) longitude
    private Double radius;      // 검색 반경 (km 단위)
    private String region1depthName;  // 시/도 (지역 1단계 명칭)
    private String region2depthName;  // 구/군 (지역 2단계 명칭)
    private String region3depthName;  // 동/읍/면 (지역 3단계 명칭)
}
