package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.entity.CaseStatus;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class LostCreateRequest extends ArticleCreateRequest {

  @NotBlank(message = "동물 타입은 필수입니다.")
  private PetType petType;

  private String breed;

  private String name;

  private Integer petAge;

  private PetGender petGender;

  private Boolean isNeutering;

  private String petColor;

  private String chipNumber;

  private LocalDateTime lostDate;

  @NotBlank(message = "실종 장소는 필수입니다.")
  private String lostLocation;

  private String contact;

  @NotBlank(message = "실종 상태는 필수입니다.")
  private CaseStatus lostStatus;


}
