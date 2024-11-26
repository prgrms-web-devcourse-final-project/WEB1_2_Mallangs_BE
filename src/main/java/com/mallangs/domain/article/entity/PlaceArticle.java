package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.PlaceCreateRequest;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("place")
@SuperBuilder
public class PlaceArticle extends Article {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long placeId;
//
//  @Column(length = 50)
//  private String name;

  @Column(length = 255)
  private String businessHours;

  @Column(length = 50)
  private String closeDays;

  @Column(nullable = true, columnDefinition = "TEXT")
  private String website; //웹사이트링크


  @Override
  public void applyChanges(Article updatedArticle) {
    super.applyChanges(updatedArticle);

    if (businessHours != null) {
      this.businessHours = updatedArticle
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