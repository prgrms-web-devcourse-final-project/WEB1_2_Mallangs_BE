package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.entity.LostStatus;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LostCreateRequest extends ArticleCreateRequest {

  @NotNull
  private PetType petType;

  private String breed;

  private String petName;

  private Integer petAge;

  private PetGender petGender;

  private Boolean neutered;

  private String chipNumber;

  private LocalDate lostDate;

  private String lastSeenLocation;

  @NotNull
  private LostStatus lostStatus;


}
