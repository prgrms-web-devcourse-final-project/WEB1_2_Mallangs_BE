package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.RescueCreateRequest;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("rescue")
@SuperBuilder
public class RescueArticle extends Article {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PetType petType;

  @Override
  public void applyChanges(Article updatedArticle) {
    super.applyChanges(updatedArticle);

    if (updatedArticle instanceof RescueArticle) {
      RescueArticle updatedRescueArticle = (RescueArticle) updatedArticle;
      if (updatedRescueArticle.getPetType() != null) {
        this.petType = updatedRescueArticle.getPetType();
      }
    }
  }

  public static RescueArticle createRescueArticle(Member member,
      RescueCreateRequest createRequest) {
    // GeometryFactory 객체 생성
    GeometryFactory geometryFactory = new GeometryFactory();

    // 위도와 경도를 기반으로 Coordinate 객체 생성
    Coordinate coordinate = new Coordinate(createRequest.getLongitude(),
        createRequest.getLatitude());

    // Point 객체 생성
    Point geography = geometryFactory.createPoint(coordinate);
    geography.setSRID(4326);  // SRID 4326 (WGS 84) 설정

    return RescueArticle.builder()
        .petType(createRequest.getPetType())
        .member(member)
        .type(createRequest.getType())
        .articleStatus(createRequest.getArticleStatus())
        .title(createRequest.getTitle())
        .geography(geography)
        .description(createRequest.getDescription())
        .image(createRequest.getImage())
        .build();
  }
}
