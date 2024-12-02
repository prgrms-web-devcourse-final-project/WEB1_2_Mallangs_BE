package com.mallangs.domain.article.service;

import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import com.mallangs.domain.article.repository.ArticleRepository;
import com.mallangs.domain.article.repository.LocationRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

  private final LocationRepository locationRepository;

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

  public List<MapBoundsResponse> findArticlesInBoundsByType(double southWestLat,
      double southWestLon,
      double northEastLat, double northEastLon, String type) {

    log.info("findArticlesInBounds");
    log.info(String.valueOf(southWestLat));
    log.info(String.valueOf(southWestLon));
    log.info(String.valueOf(northEastLat));
    log.info(String.valueOf(northEastLon));

    return locationRepository.findArticlesInBoundsByType(southWestLat, southWestLon, northEastLat,
        northEastLon, type);
  }

}
