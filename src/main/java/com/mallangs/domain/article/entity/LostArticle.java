package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.LostCreateRequest;
import com.mallangs.domain.member.Member;
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

  private String petName;

  private Integer petAge;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PetGender petGender;

  private Boolean neutered;

  private String chipNumber;

  private LocalDate lostDate;

  private String lastSeenLocation;

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
      if (updatedLostArticle.getPetName() != null) {
        this.petName = updatedLostArticle.getPetName();
      }
      if (updatedLostArticle.getPetAge() != null) {
        this.petAge = updatedLostArticle.getPetAge();
      }
      if (updatedLostArticle.getPetGender() != null) {
        this.petGender = updatedLostArticle.getPetGender();
      }
      if (updatedLostArticle.getNeutered() != null) {
        this.neutered = updatedLostArticle.getNeutered();
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
      if (updatedLostArticle.getLostStatus() != null) {
        this.lostStatus = updatedLostArticle.getLostStatus();
      }
    }
  }

  public static LostArticle createLostArticle(Member member, LostCreateRequest createRequest) {
    return LostArticle.builder()
        .petType(createRequest.getPetType())
        .breed(createRequest.getBreed())
        .petName(createRequest.getPetName())
        .petAge(createRequest.getPetAge())
        .petGender(createRequest.getPetGender())
        .neutered(createRequest.getNeutered())
        .chipNumber(createRequest.getChipNumber())
        .lostDate(createRequest.getLostDate())
        .lastSeenLocation(createRequest.getLastSeenLocation())
        .lostStatus(createRequest.getLostStatus())
        .member(member)
        .mapVisibility(createRequest.getMapVisibility())
        .title(createRequest.getTitle())
        .geography(createRequest.getGeography())
        .description(createRequest.getDescription())
        .contact(createRequest.getContact())
        .image(createRequest.getImage())
        .build();
  }
}
