package com.mallangs.domain.article.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MapBoundsRequest {

  @NotNull(message = "북동쪽 위도 값은 필수입니다.")
  private Double northEastLat;

  @NotNull(message = "북동쪽 경도 값은 필수입니다.")
  private Double northEastLon;

  @NotNull(message = "남서쪽 위도 값은 필수입니다.")
  private Double southWestLat;

  @NotNull(message = "남서쪽 경도 값은 필수입니다.")
  private Double southWestLon;

}
