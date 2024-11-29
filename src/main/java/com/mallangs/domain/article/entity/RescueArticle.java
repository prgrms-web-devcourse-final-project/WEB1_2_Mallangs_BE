package com.mallangs.domain.article.entity;

import com.mallangs.domain.article.dto.request.RescueCreateRequest;
import com.mallangs.domain.member.Member;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    return RescueArticle.builder()
        .petType(createRequest.getPetType())
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
