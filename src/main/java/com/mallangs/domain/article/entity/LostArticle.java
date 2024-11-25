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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("lost")
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


}
