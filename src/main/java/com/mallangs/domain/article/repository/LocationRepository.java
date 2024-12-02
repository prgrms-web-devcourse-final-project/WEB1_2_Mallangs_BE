package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Article, Long> {

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

  @Query(value = "SELECT a.* FROM Article a WHERE ST_Intersects(a.geography, ST_GeomFromText(:polygonWkt))", nativeQuery = true)
  List<Article> findArticlesInBounds(@Param("polygonWkt") String polygonWkt);


}
