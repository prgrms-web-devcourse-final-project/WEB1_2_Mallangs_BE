package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.PlaceArticle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class PlaceResponse extends ArticleResponse {

  private String businessHours;

  private String closeDays;

  private String website;

  private String contact;

  public PlaceResponse(PlaceArticle article) {
    super(article);
    this.businessHours = article.getBusinessHours();
    this.closeDays = article.getCloseDays();
    this.website = article.getWebsite();
    this.contact = article.getContact();
  }

}
