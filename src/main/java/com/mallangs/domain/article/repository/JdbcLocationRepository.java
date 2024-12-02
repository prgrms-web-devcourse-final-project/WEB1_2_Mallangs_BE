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

//  @Override
//  public List<MapBoundsResponse> findArticlesInBounds(String polygonWkt) {
////    String query = "SELECT * FROM article a " +
////        "WHERE MBRContains(ST_GeomFromText('POLYGON((" + polygonWkt + "))', 4326), a.geography)";
//    String query = "SELECT a.article_id, a.type, a.title, " +
//        "ST_X(a.geography) AS latitude, " +
//        "ST_Y(a.geography) AS longitude, " +
//        "a.description " +
//        "FROM article a " +
//        "WHERE MBRContains(ST_GeomFromText('POLYGON((" + polygonWkt + "))', 4326), a.geography)";
//
//    log.info(query);
//
//    return jdbcTemplate.query(query, (rs, rowNum) ->
//        new MapBoundsResponse(
//            rs.getLong("article_id"),
//            rs.getString("type"),
//            rs.getString("title"),
//            rs.getDouble("latitude"), // geography의 x 값
//            rs.getDouble("longtitude"),  // geography의 y 값
//            rs.getString("description")
//        )
//    );
//  }

  @Override
  public List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon) {
//    String query = "SELECT * FROM article a " +
//        "WHERE MBRContains(ST_GeomFromText('POLYGON((" + polygonWkt + "))', 4326), a.geography)";
    String query = String.format(
        "SELECT a.article_id, a.type, a.title, ST_X(a.geography) AS latitude, " +
            "ST_Y(a.geography) AS longitude, a.description " +
            "FROM article a " +
            "WHERE MBRContains(ST_GeomFromText('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))', 4326), a.geography)",
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

}
