package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.CaseStatus;
import com.mallangs.domain.article.entity.LostArticle;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LostResponse extends ArticleResponse {

  private PetType petType;

  private String breed;

  private String name;

  private Integer petAge;

  private PetGender petGender;

  private Boolean isNeutering;

  private String petColor;

  private String chipNumber;

  private LocalDateTime lostDate;

  private String lostLocation;

  private String contact;

  private CaseStatus lostStatus;

  public LostResponse(LostArticle article) {
    super(article);
    this.petType = article.getPetType();
    this.breed = article.getBreed();
    this.name = article.getName();
    this.petAge = article.getPetAge();
    this.petGender = article.getPetGender();
    this.isNeutering = article.getIsNeutering();
    this.petColor = article.getPetColor();
    this.chipNumber = article.getChipNumber();
    this.lostDate = article.getLostDate();
    this.lostLocation = article.getLostLocation();
    this.contact = article.getContact();
    this.lostStatus = article.getLostStatus();
  }


}
