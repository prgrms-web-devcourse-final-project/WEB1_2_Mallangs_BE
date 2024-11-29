package com.mallangs.domain.article.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class PlaceCreateRequest extends ArticleCreateRequest {

  private String businessHours;

  private String closeDays;

  private String website;

  private String contact;


}
