package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@AllArgsConstructor
public class JdbcLocationRepository implements LocationRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon) {

    String query = String.format(
        "SELECT a.article_id, a.type, a.title, ST_X(a.geography) AS latitude, " +
            "ST_Y(a.geography) AS longitude, a.description " +
            "FROM article a " +
            "WHERE MBRContains(ST_GeomFromText('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))', 4326), a.geography) "
            + "AND a.map_visibility = 'VISIBLE'",
        southWestLat, southWestLon,
        southWestLat, northEastLon,
        northEastLat, northEastLon,
        northEastLat, southWestLon,
        southWestLat, southWestLon);

    log.info(query);

    return jdbcTemplate.query(query, (rs, rowNum) ->
        new MapBoundsResponse(
            rs.getLong("article_id"),
            rs.getString("type"),
            rs.getString("title"),
            rs.getDouble("latitude"), // geography의 x 값
            rs.getDouble("longitude"),  // geography의 y 값
            rs.getString("description")
        )
    );
  }

  // 실종 구조
  @Override
  public List<MapBoundsResponse> findArticlesInBoundsByType(double southWestLat,
      double southWestLon, double northEastLat, double northEastLon, String type) {
    // 타입별 조회
    String query = String.format(
        "SELECT a.article_id, a.type, a.title, ST_X(a.geography) AS latitude, " +
            "ST_Y(a.geography) AS longitude, a.description " +
            "FROM article a " +
            "WHERE MBRContains(ST_GeomFromText('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))', 4326), a.geography) "
            + "AND a.type = '%s' AND a.map_visibility = 'VISIBLE'",
        southWestLat, southWestLon,
        southWestLat, northEastLon,
        northEastLat, northEastLon,
        northEastLat, southWestLon,
        southWestLat, southWestLon,
        type);

    log.info(query);

    return jdbcTemplate.query(query, (rs, rowNum) ->
        new MapBoundsResponse(
            rs.getLong("article_id"),
            rs.getString("type"),
            rs.getString("title"),
            rs.getDouble("latitude"), // geography의 x 값
            rs.getDouble("longitude"),  // geography의 y 값
            rs.getString("description")
        )
    );
  }

  // 장소 / 사용자 등록 위치
  @Override
  public List<MapBoundsResponse> findPlaceArticlesInBoundsByType(double southWestLat,
      double southWestLon, double northEastLat, double northEastLon, boolean isPublicData) {

    String query = String.format(
        "SELECT a.article_id, a.type, a.title, ST_X(a.geography) AS latitude, " +
            "ST_Y(a.geography) AS longitude, a.description " +
            "FROM place_article p "
            + "JOIN article a ON p.article_id = a.article_id "
            + "WHERE MBRContains(ST_GeomFromText('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))', 4326), a.geography) "
            + "AND p.is_public_data = %b AND a.map_visibility = 'VISIBLE'",
        southWestLat, southWestLon,
        southWestLat, northEastLon,
        northEastLat, northEastLon,
        northEastLat, southWestLon,
        southWestLat, southWestLon,
        isPublicData);

    log.info(query);

    return jdbcTemplate.query(query, (rs, rowNum) ->
        new MapBoundsResponse(
            rs.getLong("article_id"),
            rs.getString("type"),
            rs.getString("title"),
            rs.getDouble("latitude"), // geography의 x 값
            rs.getDouble("longitude"),  // geography의 y 값
            rs.getString("description")
        )
    );
  }


  @Override
  public List<MapBoundsResponse> findPlaceArticlesInBoundsByCategory(double southWestLat,
      double southWestLon, double northEastLat, double northEastLon,
      String placeCategory, boolean isPublicData) {

    String query = String.format(
        "SELECT a.article_id, a.type, a.title, ST_X(a.geography) AS latitude, " +
            "ST_Y(a.geography) AS longitude, a.description " +
            "FROM place_article p "
            + "JOIN article a ON p.article_id = a.article_id "
            + "WHERE MBRContains(ST_GeomFromText('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))', 4326), a.geography) "
            + "AND p.category = '%s' AND p.is_public_data = %b AND a.map_visibility = 'VISIBLE'",
        southWestLat, southWestLon,
        southWestLat, northEastLon,
        northEastLat, northEastLon,
        northEastLat, southWestLon,
        southWestLat, southWestLon,
        placeCategory, isPublicData);

    log.info(query);

    return jdbcTemplate.query(query, (rs, rowNum) ->
        new MapBoundsResponse(
            rs.getLong("article_id"),
            rs.getString("type"),
            rs.getString("title"),
            rs.getDouble("latitude"), // geography의 x 값
            rs.getDouble("longitude"),  // geography의 y 값
            rs.getString("description")
        )
    );
  }

}
