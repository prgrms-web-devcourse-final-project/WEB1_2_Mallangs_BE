package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RescueCreateRequest extends ArticleCreateRequest {

  private PetType petType;

}
