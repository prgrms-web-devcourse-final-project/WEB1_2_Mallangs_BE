package com.mallangs.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MapBoundsRequest {

  @NotBlank(message = "북동쪽 위도 값은 필수입니다.")
  private double northEastLat;

  @NotBlank(message = "북동쪽 경도 값은 필수입니다.")
  private double northEastLon;

  @NotBlank(message = "남서쪽 위도 값은 필수입니다.")
  private double southWestLat;

  @NotBlank(message = "남서쪽 경도 값은 필수입니다.")
  private double southWestLon;

}
