package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.LostCreateRequest;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("lost")
@SuperBuilder
public class LostArticle extends Article {

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PetType petType;

  private String breed;

  private String name;

  private Integer petAge;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PetGender petGender;

  private Boolean isNeutering;

  private String chipNumber;

  private LocalDate lostDate;

  private String lastSeenLocation;

  private String contact;

  @Enumerated(EnumType.STRING)
  @Column(name = "lost_status", nullable = false)
  private LostStatus lostStatus;

  @Override
  public void applyChanges(Article updatedArticle) {
    super.applyChanges(updatedArticle);

    if (updatedArticle instanceof LostArticle) {
      LostArticle updatedLostArticle = (LostArticle) updatedArticle;
      if (updatedLostArticle.getPetType() != null) {
        this.petType = updatedLostArticle.getPetType();
      }
      if (updatedLostArticle.getBreed() != null) {
        this.breed = updatedLostArticle.getBreed();
      }
      if (updatedLostArticle.getName() != null) {
        this.name = updatedLostArticle.getName();
      }
      if (updatedLostArticle.getPetAge() != null) {
        this.petAge = updatedLostArticle.getPetAge();
      }
      if (updatedLostArticle.getPetGender() != null) {
        this.petGender = updatedLostArticle.getPetGender();
      }
      if (updatedLostArticle.getIsNeutering() != null) {
        this.isNeutering = updatedLostArticle.getIsNeutering();
      }
      if (updatedLostArticle.getChipNumber() != null) {
        this.chipNumber = updatedLostArticle.getChipNumber();
      }
      if (updatedLostArticle.getLostDate() != null) {
        this.lostDate = updatedLostArticle.getLostDate();
      }
      if (updatedLostArticle.getLastSeenLocation() != null) {
        this.lastSeenLocation = updatedLostArticle.getLastSeenLocation();
      }
      if (updatedLostArticle.getContact() != null) {
        this.contact = updatedLostArticle.getContact();
      }
      if (updatedLostArticle.getLostStatus() != null) {
        this.lostStatus = updatedLostArticle.getLostStatus();
      }
    }
  }

  public static LostArticle createLostArticle(Member member, LostCreateRequest createRequest) {
    // GeometryFactory 객체 생성
    GeometryFactory geometryFactory = new GeometryFactory();

    // 위도와 경도를 기반으로 Coordinate 객체 생성
    Coordinate coordinate = new Coordinate(createRequest.getLongitude(),
        createRequest.getLatitude());

    // Point 객체 생성
    Point geography = geometryFactory.createPoint(coordinate);
    geography.setSRID(4326);  // SRID 4326 (WGS 84) 설정

    return LostArticle.builder()
        .petType(createRequest.getPetType())
        .breed(createRequest.getBreed())
        .name(createRequest.getName())
        .petAge(createRequest.getPetAge())
        .petGender(createRequest.getPetGender())
        .isNeutering(createRequest.getIsNeutering())
        .chipNumber(createRequest.getChipNumber())
        .lostDate(createRequest.getLostDate())
        .lastSeenLocation(createRequest.getLastSeenLocation())
        .contact(createRequest.getContact())
        .lostStatus(createRequest.getLostStatus())
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
