package com.mallangs.domain.article.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MapBoundsRequest {

  private double northEastLat;

  private double northEastLon;

  private double southWestLat;

  private double southWestLon;

}
