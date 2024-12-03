package com.mallangs.domain.article.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "자식 클래스 DTO", allOf = ArticleCreateRequest.class)
public class PlaceCreateRequest extends ArticleCreateRequest {

  private String businessHours;

  private String closeDays;

  private String website;

  private String category;

  private String address; // 주소

  private String roadAddress; // 주소

  private Boolean hasParking; // 주차 가능 여부

  private Boolean isPetFriendly; // 반려동물 동반 가능 여부

  private String contact;

  @NotNull
  private Boolean isPublicData;


}
