package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.RescueArticle;
import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RescueResponse extends ArticleResponse {

  private PetType petType;

  public RescueResponse(RescueArticle article) {
    super(article);
    this.petType = article.getPetType();
  }

}
