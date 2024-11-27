package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.PlaceCreateRequest;
import com.mallangs.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("place")
@SuperBuilder
public class PlaceArticle extends Article {

  @Column(length = 255)
  private String businessHours;

  @Column(length = 50)
  private String closeDays;

  @Column(nullable = true, columnDefinition = "TEXT")
  private String website; //웹사이트링크


  @Override
  public void applyChanges(Article updatedArticle) {
    super.applyChanges(updatedArticle);

    PlaceArticle updatedPlaceArticle = (PlaceArticle) updatedArticle;

    if (updatedPlaceArticle.getBusinessHours() != null) {
      this.businessHours = updatedPlaceArticle.getBusinessHours();
    }
    if (updatedPlaceArticle.getCloseDays() != null) {
      this.closeDays = updatedPlaceArticle.getCloseDays();
    }
    if (updatedPlaceArticle.getWebsite() != null) {
      this.website = updatedPlaceArticle.getWebsite();
    }
  }

  public static PlaceArticle createPlaceArticle(Member member, PlaceCreateRequest createRequest) {
    return PlaceArticle.builder()
        .businessHours(createRequest.getBusinessHours())
        .closeDays(createRequest.getCloseDays())
        .website(createRequest.getWebsite())
        .build();
  }
}