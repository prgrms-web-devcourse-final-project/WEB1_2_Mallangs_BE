package com.mallangs.domain.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MapBoundsResponse {

  private Long articleId;

  private String type;

  private String title; // 장소인 경우 장소이름

  private double latitude;

  private double longitude;

  private String description;

  public MapBoundsResponse(Long articleId, String type, double latitude, double longitude, String description) {
    this.articleId = articleId;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
    this.description = description;
  }
}
