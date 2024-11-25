package com.mallangs.domain.article.entity;

import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
