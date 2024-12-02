package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.Article;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MapBoundsResponse {

  private Long articleId;

  private String type;

  private String title; // 장소인 경우 장소이름

  private double latitude;

  private double longitude;

  private String description;

  public MapBoundsResponse(Article article) {
    this.articleId = article.getId();
    this.type = article.getType();
    this.title = article.getTitle();
    this.latitude = article.getGeography().getY();
    this.longitude = article.getGeography().getX();
    this.description = article.getDescription();
  }

}
