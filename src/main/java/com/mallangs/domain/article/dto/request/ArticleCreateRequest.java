package com.mallangs.domain.article.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mallangs.domain.article.entity.ArticleType;
import com.mallangs.domain.article.validation.ValidationGroups;
import com.mallangs.domain.board.entity.BoardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

  @NotNull(message = "글 타입은 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  @JsonProperty("type")
  private ArticleType articleType; // 글타래 종류

  @NotNull(message = "글 상태는 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  private BoardStatus articleStatus;

  @NotBlank(message = "제목은 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  private String title; // 장소인 경우 장소이름

  @NotNull(message = "위도는 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다.")
  @DecimalMax(value = "90.0", message = "위도는 90.0 이하이어야 합니다.")
  private Double latitude;

  @NotNull(message = "경도는 필수입니다.", groups = ValidationGroups.CreateGroup.class)
  @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다.")
  @DecimalMax(value = "180.0", message = "경도는 180.0 이하이어야 합니다.")
  private Double longitude;

  private String description;

  private String image;


}
