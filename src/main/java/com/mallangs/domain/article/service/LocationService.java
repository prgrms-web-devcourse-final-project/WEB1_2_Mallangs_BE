package com.mallangs.domain.article.service;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private static final Logger log = LoggerFactory.getLogger(LocationService.class);
  private final LocationRepository locationRepository;

  public List<Article> findArticlesInBounds(double northEastLat, double northEastLon,
      double southWestLat, double southWestLon) {
    // Define the coordinates of the bounding box polygon
    Coordinate[] coordinates = new Coordinate[]{
        new Coordinate(southWestLon, southWestLat), // SW corner
        new Coordinate(northEastLon, southWestLat), // SE corner
        new Coordinate(northEastLon, northEastLat), // NE corner
        new Coordinate(southWestLon, northEastLat), // NW corner
        new Coordinate(southWestLon, southWestLat)  // Closing the polygon (SW corner again)
    };

    // Create a GeometryFactory to generate the polygon
    GeometryFactory geometryFactory = new GeometryFactory();
    Polygon polygon = geometryFactory.createPolygon(coordinates);
    polygon.setSRID(4326); // Set the SRID (Spatial Reference System Identifier)

    String polygonWkt = "SRID=4326;" + polygon.toText();

    log.info("Find articles in bounds using polygon: " + polygonWkt);

    // Query the repository to find articles in the bounds defined by the polygon
    return locationRepository.findArticlesInBounds(polygonWkt);

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
  }

}
