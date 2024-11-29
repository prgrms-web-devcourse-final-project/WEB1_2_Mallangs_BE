package com.mallangs.domain.article.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class PlaceCreateRequest extends ArticleCreateRequest {

  private String businessHours;

  private String closeDays;

  private String website;


}
