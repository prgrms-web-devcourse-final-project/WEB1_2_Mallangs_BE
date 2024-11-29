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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

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

  private String contact;

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
    if (updatedPlaceArticle.getContact() != null) {
      this.contact = updatedPlaceArticle.getContact();
    }
  }

  public static PlaceArticle createPlaceArticle(Member member, PlaceCreateRequest createRequest) {
    // GeometryFactory 객체 생성
    GeometryFactory geometryFactory = new GeometryFactory();

    // 위도와 경도를 기반으로 Coordinate 객체 생성
    Coordinate coordinate = new Coordinate(createRequest.getLongitude(),
        createRequest.getLatitude());

    // Point 객체 생성
    Point geography = geometryFactory.createPoint(coordinate);
    geography.setSRID(4326);  // SRID 4326 (WGS 84) 설정

    return PlaceArticle.builder()
        .businessHours(createRequest.getBusinessHours())
        .closeDays(createRequest.getCloseDays())
        .website(createRequest.getWebsite())
        .contact(createRequest.getContact())
        .member(member)
        .type(createRequest.getArticleType())
        .mapVisibility(createRequest.getMapVisibility())
        .title(createRequest.getTitle())
        .geography(geography)
        .description(createRequest.getDescription())
        .image(createRequest.getImage())
        .build();
  }
}