package com.mallangs.domain.article.dto.request;

import com.mallangs.domain.article.entity.MapVisibility;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
@RequiredArgsConstructor
public class ArticleCreateRequest {

  @NotNull
  private String articleType;

  @NotNull
  private Long memberId;

  @NotNull
  private MapVisibility mapVisibility;

  @NotNull
  private String title; // 장소인 경우 장소이름

  @NotNull
  private Point geography;

  private String description;

  private String contact;

  private String image;


}
