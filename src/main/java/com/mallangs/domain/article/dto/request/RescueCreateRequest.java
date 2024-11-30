package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.pet.entity.PetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class RescueCreateRequest extends ArticleCreateRequest {

  private PetType petType;

}
