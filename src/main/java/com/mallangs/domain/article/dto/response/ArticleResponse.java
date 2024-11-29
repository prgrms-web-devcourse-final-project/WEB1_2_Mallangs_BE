package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.MapVisibility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
@RequiredArgsConstructor
public class ArticleResponse {

  private Long articleId;

//  private String articleType;

  private Long memberId;

  private MapVisibility mapVisibility;

  private String title; // 장소인 경우 장소이름

  private Point geography;

  private String description;

  private String contact;

  private String image;

  public ArticleResponse(Article article) {
    this.articleId = article.getId();
    this.memberId = article.getMember().getMemberId();
    this.mapVisibility = article.getMapVisibility();
    this.title = article.getTitle();
    this.geography = article.getGeography();
    this.description = article.getDescription();
    this.contact = article.getContact();
    this.image = article.getImage();
  }

}
