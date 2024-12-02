package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.RescueCreateRequest;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CaseStatus rescueStatus;

  private String rescueLocation;

  private LocalDateTime rescueDate;

  @Override
  public void applyChanges(Article updatedArticle) {
    super.applyChanges(updatedArticle);

    if (updatedArticle instanceof RescueArticle) {
      RescueArticle updatedRescueArticle = (RescueArticle) updatedArticle;
      if (updatedRescueArticle.getPetType() != null) {
        this.petType = updatedRescueArticle.getPetType();
      }
      if (updatedRescueArticle.getRescueStatus() != null) {
        this.rescueStatus = updatedRescueArticle.getRescueStatus();
      }
      if (updatedRescueArticle.getRescueLocation() != null) {
        this.rescueLocation = updatedRescueArticle.getRescueLocation();
      }
      if (updatedRescueArticle.getRescueDate() != null) {
        this.rescueDate = updatedRescueArticle.getRescueDate();
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
        .rescueStatus(createRequest.getRescueStatus())
        .rescueLocation(createRequest.getRescueLocation())
        .rescueDate(createRequest.getRescueDate())
        .member(member)
        .articleType(createRequest.getArticleType())
        .articleStatus(createRequest.getArticleStatus())
        .title(createRequest.getTitle())
        .geography(geography)
        .description(createRequest.getDescription())
        .image(createRequest.getImage())
        .build();
  }
}
