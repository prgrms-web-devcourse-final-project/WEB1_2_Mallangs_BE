package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.MapVisibility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ArticleResponse {

  private Long articleId;

  private String articleType;

  private Long memberId;

  private MapVisibility mapVisibility;

  private String title; // 장소인 경우 장소이름

  private double latitude;

  private double longitude;

  private String description;

  private String image;

  public ArticleResponse(Article article) {
    this.articleId = article.getId();
    this.articleType = article.getType();
    this.memberId = article.getMember().getMemberId();
    this.mapVisibility = article.getMapVisibility();
    this.title = article.getTitle();
    this.latitude = article.getGeography().getY();
    this.longitude = article.getGeography().getX();
    this.description = article.getDescription();
    this.image = article.getImage();
  }

}
