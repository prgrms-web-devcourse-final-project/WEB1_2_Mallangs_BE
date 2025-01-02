package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.dto.response.MapBoundsResponse;
import java.util.List;

public interface LocationRepository {

  List<MapBoundsResponse> findArticlesInBounds(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon);

  List<MapBoundsResponse> findArticlesInBoundsByType(double southWestLat, double southWestLon,
      double northEastLat, double northEastLon, String type);

  List<MapBoundsResponse> findPlaceArticlesInBoundsByType(double southWestLat,
      double southWestLon, double northEastLat, double northEastLon, boolean isPublicData);

  List<MapBoundsResponse> findPlaceArticlesInBoundsByCategory(double southWestLat,
      double southWestLon,
      double northEastLat, double northEastLon, String placeCategory, boolean isPublicData);

  List<MapBoundsResponse> findUserInBoundsByCategory(double southWestLat,
      double southWestLon, double northEastLat, double northEastLon);

}
