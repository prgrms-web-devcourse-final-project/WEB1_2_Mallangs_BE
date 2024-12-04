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

  private String category;

  private String address; // 주소

  private String roadAddress; // 주소

  private Boolean hasParking; // 주차 가능 여부

  private Boolean isPetFriendly; // 반려동물 동반 가능 여부

  private String contact;

  private Boolean isPublicData; // 공공데이터 사용자등록데이터 구분

  public PlaceResponse(PlaceArticle article) {
    super(article);
    this.businessHours = article.getBusinessHours();
    this.closeDays = article.getCloseDays();
    this.website = article.getWebsite();
    this.category = article.getCategory();
    this.address = article.getAddress();
    this.roadAddress = article.getRoadAddress();
    this.hasParking = article.getHasParking();
    this.isPetFriendly = article.getIsPetFriendly();
    this.contact = article.getContact();
    this.isPublicData = article.getIsPublicData();
  }

}
