package com.mallangs.domain.pet.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

@Getter
@NoArgsConstructor
@Setter
public class PetLocationRequest {
    private Double y;    // 위도 (y 좌표) latitude
    private Double x;   // 경도 (x 좌표) longitude
//    private Point point; //위도 경도를 Point 에 담아서 사용
    private Double radius;      // 검색 반경 (km 단위)
    private String region1depthName;  // 시/도 (지역 1단계 명칭)
    private String region2depthName;  // 구/군 (지역 2단계 명칭)
    private String region3depthName;  // 동/읍/면 (지역 3단계 명칭)



    // 생성자를 통해 Point를 설정할 수 있도록
    public PetLocationRequest(double x, double y, Double radius,
                              String region1depthName,
                              String region2depthName,
                              String region3depthName) {
//        this.point = new Point(x, y);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.region3depthName = region3depthName;
    }

}
