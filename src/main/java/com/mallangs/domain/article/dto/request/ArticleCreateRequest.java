package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.entity.MapVisibility;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Schema(description = "부모 클래스 DTO")
public class ArticleCreateRequest {

  @NotNull
  private String articleType;

  @NotNull
  private MapVisibility mapVisibility;

  @NotNull
  private String title; // 장소인 경우 장소이름

  @NotNull
  private double latitude;

  @NotNull
  private double longitude;

  private String description;

  private String image;


}
