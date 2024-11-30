package com.mallangs.domain.article.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.mallangs.domain.board.entity.BoardStatus;
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
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = As.PROPERTY,
    property = "type",  // 이 필드에 따라 자식 클래스로 변환
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LostCreateRequest.class, name = "lost"),
    @JsonSubTypes.Type(value = RescueCreateRequest.class, name = "rescue"),
    @JsonSubTypes.Type(value = PlaceCreateRequest.class, name = "place")
})
public class ArticleCreateRequest {

  @NotNull
  @JsonProperty("type")
  private String type; // 글타래 종류

  @NotNull
  private BoardStatus articleStatus;

  @NotNull
  private String title; // 장소인 경우 장소이름

  @NotNull
  private double latitude;

  @NotNull
  private double longitude;

  private String description;

  private String image;


}
