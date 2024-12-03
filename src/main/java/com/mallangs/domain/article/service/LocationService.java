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

  private final EntityManager em;
  private final LocationRepository locationRepository;
  private final ArticleRepository articleRepository;

  public List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon) {
    // 위도, 경도 순서로 전달해야 하므로, 순서를 바꿔서 호출
    log.info("findArticlesInBounds");
    log.info(String.valueOf(southWestLat));
    log.info(String.valueOf(southWestLon));
    log.info(String.valueOf(northEastLat));
    log.info(String.valueOf(northEastLon));

//    String polygonWkt = String.format(
//        "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
//        southWestLat, southWestLon,
//        southWestLat, northEastLon,
//        northEastLat, northEastLon,
//        northEastLat, southWestLon,
//        southWestLat, southWestLon);

//    String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", northEastLat, northEastLon,
//        southWestLat, southWestLon);
//    Query query = em.createNativeQuery(
//        "SELECT a FROM article a WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat
//            + ", r.point)", Article.class);
//
//    List<Article> articles = query.getResultList();
//    return articles;

    return locationRepository.findArticlesInBounds(southWestLat, southWestLon, northEastLat,
        northEastLon);
  }
//  public List<Article> findArticlesInBounds(double northEastLat, double northEastLon,
//      double southWestLat, double southWestLon) {
//    Coordinate[] coordinates = new Coordinate[]{
//        new Coordinate(southWestLon, southWestLat),
//        new Coordinate(northEastLon, southWestLat),
//        new Coordinate(northEastLon, northEastLat),
//        new Coordinate(southWestLon, northEastLat),
//        new Coordinate(southWestLon, southWestLat)
//    };
//
//    GeometryFactory geometryFactory = new GeometryFactory();
//    Polygon polygon = geometryFactory.createPolygon(coordinates);
//    polygon.setSRID(4326);
//
//    String polygonWkt = "SRID=4326;" + polygon.toText();
//
//    log.info("Find articles in bounds using polygon: " + polygonWkt);
//
//    return locationRepository.findArticlesInBounds(southWestLon, southWestLat, northEastLon,
//        northEastLat);

//    String polygonWkt = String.format(
//        "SRID=4326;POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
//        southWestLat, southWestLon, northEastLat, southWestLon,
//        northEastLat, northEastLon, southWestLat, northEastLon,
//        southWestLat, southWestLon);
//
//    log.info("Find articles in bounds " + polygonWkt);
//
//    return locationRepository.findArticlesInBounds(polygonWkt);

//    return locationRepository.findArticlesInBounds(northEastLat, northEastLon, southWestLat,
//        southWestLon);
//  }

}
