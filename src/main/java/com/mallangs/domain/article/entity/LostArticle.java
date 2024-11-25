package com.mallangs.domain.article.entity;

import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
