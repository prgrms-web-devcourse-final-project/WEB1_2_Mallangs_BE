package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.entity.LostStatus;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class LostCreateRequest extends ArticleCreateRequest {

  @NotNull
  private PetType petType;

  private String breed;

  private String name;

  private Integer petAge;

  private PetGender petGender;

  private Boolean isNeutering;

  private String chipNumber;

  private LocalDate lostDate;

  private String lastSeenLocation;

  private String contact;

  @NotNull
  private LostStatus lostStatus;


}
