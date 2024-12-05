package com.mallangs.domain.article.service;

import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import com.mallangs.domain.article.repository.LocationRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

  private final LocationRepository locationRepository;

  // 지도 조회 시 지도 표시, 글상태 체크
  // 사용자 지도, 목록 조회 시 표시, 발생만 반환
  // 관리자 목록 형태 조회 시 다 볼 수 있음

  // 전체 조회
  public List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon) {
    // 위도, 경도 순서로 전달해야 하므로, 순서를 바꿔서 호출
    log.info("findArticlesInBounds");
    log.info(String.valueOf(southWestLat));
    log.info(String.valueOf(southWestLon));
    log.info(String.valueOf(northEastLat));
    log.info(String.valueOf(northEastLon));

    return locationRepository.findArticlesInBounds(southWestLat, southWestLon, northEastLat,
        northEastLon);
  }

  // 대분류 기준 조회
  public List<MapBoundsResponse> findArticlesInBoundsByType(double southWestLat,
      double southWestLon,
      double northEastLat, double northEastLon, String type) {

    log.info("findArticlesInBounds");
    log.info(String.valueOf(southWestLat));
    log.info(String.valueOf(southWestLon));
    log.info(String.valueOf(northEastLat));
    log.info(String.valueOf(northEastLon));

    // 장소 public, user 구분
    if (Objects.equals(type, "place") || Objects.equals(type, "user")) {
      boolean isPublicData = Objects.equals(type, "place"); // place 면 true

      return locationRepository.findPlaceArticlesInBoundsByType(southWestLat,
          southWestLon, northEastLat, northEastLon, isPublicData);
    }

    // 실종, 구조
    return locationRepository.findArticlesInBoundsByType(
        southWestLat, southWestLon,
        northEastLat, northEastLon,
        type);
  }

  // 장소 소분류 기준 조회
  public List<MapBoundsResponse> findPlaceArticlesInBoundsByCategory(
      double southWestLat, double southWestLon,
      double northEastLat, double northEastLon,
      String articleType, String placeCategory) {

    log.info("findArticlesInBounds");
    log.info(String.valueOf(southWestLat));
    log.info(String.valueOf(southWestLon));
    log.info(String.valueOf(northEastLat));
    log.info(String.valueOf(northEastLon));

    boolean isPublicData;

    isPublicData = articleType.equalsIgnoreCase("place");
    // true 면 공공 데이터 검색
    // false 면 사용자 등록 정보 검색

    return locationRepository.findPlaceArticlesInBoundsByCategory(
        southWestLat, southWestLon,
        northEastLat, northEastLon,
        placeCategory, isPublicData);
  }

}
