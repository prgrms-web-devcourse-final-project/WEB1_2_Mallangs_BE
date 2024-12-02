package com.mallangs.domain.article.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mallangs.domain.article.entity.ArticleType;
import com.mallangs.domain.board.entity.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",  // 이 필드에 따라 자식 클래스로 변환
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LostCreateRequest.class, name = "lost"),
    @JsonSubTypes.Type(value = RescueCreateRequest.class, name = "rescue"),
    @JsonSubTypes.Type(value = PlaceCreateRequest.class, name = "place")
})
public class ArticleCreateRequest {

  @NotBlank(message = "글 타입은 필수입니다.")
  @JsonProperty("type")
  private ArticleType articleType; // 글타래 종류

  @NotBlank(message = "글 상태는 필수입니다.")
  private BoardStatus articleStatus;

  @NotBlank(message = "제목은 필수입니다.")
  private String title; // 장소인 경우 장소이름

  @NotNull(message = "위도는 필수입니다.")
  private double latitude;

  @NotNull(message = "경도는 필수입니다.")
  private double longitude;

  private String description;

  private String image;


}
