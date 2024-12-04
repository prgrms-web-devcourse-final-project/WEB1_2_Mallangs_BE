package com.mallangs.domain.article.dto.response;

import com.mallangs.domain.article.entity.CaseStatus;
import com.mallangs.domain.article.entity.RescueArticle;
import com.mallangs.domain.pet.entity.PetType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class RescueResponse extends ArticleResponse {

  private PetType petType;

  private CaseStatus rescueStatus;

  private String rescueLocation;

  private LocalDateTime rescueDate;

  public RescueResponse(RescueArticle article) {
    super(article);
    this.petType = article.getPetType();
    this.rescueStatus = article.getRescueStatus();
    this.rescueLocation = article.getRescueLocation();
    this.rescueDate = article.getRescueDate();
  }

}
