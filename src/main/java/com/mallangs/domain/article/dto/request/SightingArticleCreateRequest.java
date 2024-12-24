package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.validation.ValidationGroups;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class SightingArticleCreateRequest extends ArticleCreateRequest {

  @NotNull
  private Long lostArticleId;

  @NotNull(message = "동물 타입은 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  private PetType petType;

  private String breed;

  private PetGender petGender;

  private String petColor;

  private String chipNumber;

  private LocalDateTime sightDate;

  @NotBlank(message = "목격 장소는 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  private String sightLocation;

}
