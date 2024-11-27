package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.LostArticle;
import com.mallangs.domain.article.entity.LostStatus;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LostResponse extends ArticleResponse {

  private PetType petType;

  private String breed;

  private String petName;

  private Integer petAge;

  private PetGender petGender;

  private Boolean neutered;

  private String chipNumber;

  private LocalDate lostDate;

  private String lastSeenLocation;

  private LostStatus lostStatus;

  public LostResponse(LostArticle article) {
    super(article);
    this.petType = article.getPetType();
    this.breed = article.getBreed();
    this.petName = article.getPetName();
    this.petAge = article.getPetAge();
    this.petGender = article.getPetGender();
    this.neutered = article.getNeutered();
    this.chipNumber = article.getChipNumber();
    this.lostDate = article.getLostDate();
    this.lastSeenLocation = article.getLastSeenLocation();
    this.lostStatus = article.getLostStatus();
  }


}
