package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import java.util.List;

public interface LocationRepository {

//  @Query("SELECT a FROM Article a WHERE ST_Within(a.geography, " +
//      "ST_GeomFromText('POLYGON((:southWestLon :southWestLat, :northEastLon :northEastLat, " +
//      ":northEastLon :southWestLat, :southWestLon :northEastLat))'))")
//  List<Article> findArticlesInBounds(@Param("northEastLat") double northEastLat,
//      @Param("northEastLon") double northEastLon,
//      @Param("southWestLat") double southWestLat,
//      @Param("southWestLon") double southWestLon);

//  @Query("SELECT a FROM Article a WHERE ST_Intersects(a.geography, ST_GeomFromText(:polygonWkt))")
//  List<Article> findArticlesInBounds(@Param("polygonWkt") String polygonWkt);

//  @Query(value = "SELECT * FROM Article a WHERE ST_Contains(ST_GeomFromText(:polygonWkt), a.geography)", nativeQuery = true)
//  List<Article> findArticlesInBounds(@Param("polygonWkt") String polygonWkt);

//  @Query(value = "SELECT * FROM Article a WHERE ST_Contains(ST_GeomFromText('SRID=4326;POLYGON((37.5640 126.9740, 37.5640 126.9780, 37.5665 126.9780, 37.5665 126.9740, 37.5640 126.9740))'), a.geography)", nativeQuery = true)
//  List<Article> findArticlesInBounds();

//  @Query(value = "SELECT a.* FROM Article a WHERE ST_Intersects(a.geography, ST_GeomFromText(:polygonWkt))", nativeQuery = true)
//  List<Article> findArticlesInBounds(@Param("polygonWkt") String polygonWkt);

//  @Query(value = "SELECT a.* FROM Article a " +
//      "WHERE ST_Within(a.geography, ST_GeomFromText('POLYGON((:swLon :swLat, :neLon :swLat, :neLon :neLat, :swLon :neLat, :swLon :swLat))'))", nativeQuery = true)
//  List<Article> findArticlesInBounds(@Param("swLon") double swLon,
//      @Param("swLat") double swLat,
//      @Param("neLon") double neLon,
//      @Param("neLat") double neLat);

//  @Query(value = "SELECT a.article_id, a.type, a.title, a.latitude, a.geography FROM Article a " +
//      "WHERE ST_Within(a.geography, ST_GeomFromText('POLYGON((37.7749 -122.4194, 37.8049 -122.4194, 37.8049 -122.3894, 37.7749 -122.3894, 37.7749 -122.4194))',4326))", nativeQuery = true)
//  List<Article> findArticlesInBound s(@Param("swLat") double swLat,
//      @Param("swLon") double swLon,
//      @Param("neLat") double neLat,
//      @Param("neLon") double neLon);

//  @Query(value =
//      "SELECT a.article_id, a.type, a.title, ST_Y(a.geography) AS latitude, ST_X(a.geography) AS longitude, a.description "
//          +
//          "FROM Article a " +
//          "WHERE ST_Within(a.geography, ST_GeomFromText('POLYGON((37.7749 -122.4194, 37.8049 -122.4194, 37.8049 -122.3894, 37.7749 -122.3894, 37.7749 -122.4194))', 4326))", nativeQuery = true)
//  List<Article> findArticlesInBounds(@Param("swLat") double swLat,
//      @Param("swLon") double swLon,
//      @Param("neLat") double neLat,
//      @Param("neLon") double neLon);

//
//  @Query(value = "SELECT a.* FROM Article a " +
//      "WHERE ST_Within(a.geography, ST_GeomFromText('POLYGON((?1 ?2, ?3 ?2, ?3 ?4, ?1 ?4, ?1 ?2))', 4326))", nativeQuery = true)
//  List<Article> findArticlesInBounds(double swLat, double swLon, double neLat, double neLon);

//
//  @Query(value = "SELECT * FROM article a " +
//      "WHERE MBRContains(ST_GeomFromText('POLYGON((:polygonWkt))', 4326), a.geography)",
//      nativeQuery = true)
//  List<Article> findArticlesInBounds(@Param("polygonWkt") String polygonWkt);

//  List<MapBoundsResponse> findArticlesInBounds(String polygonWkt);

  List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon);


}
