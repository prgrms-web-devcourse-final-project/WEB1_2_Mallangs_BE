package com.mallangs.domain.article.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MapBoundsRequest {

  @NotNull(message = "북동쪽 위도 값은 필수입니다.")
  @DecimalMin(value = "-90.0", message = "북동쪽 위도는 -90.0 이상이어야 합니다.")
  @DecimalMax(value = "90.0", message = "북동쪽 위도는 90.0 이하이어야 합니다.")
  private Double northEastLat;

  @NotNull(message = "북동쪽 경도 값은 필수입니다.")
  @DecimalMin(value = "-180.0", message = "북동쪽 경도는 -180.0 이상이어야 합니다.")
  @DecimalMax(value = "180.0", message = "북동쪽 경도는 180.0 이하이어야 합니다.")
  private Double northEastLon;

  @NotNull(message = "남서쪽 위도 값은 필수입니다.")
  @DecimalMin(value = "-90.0", message = "남서쪽 위도는 -90.0 이상이어야 합니다.")
  @DecimalMax(value = "90.0", message = "남서쪽 위도는 90.0 이하이어야 합니다.")
  private Double southWestLat;

  @NotNull(message = "남서쪽 경도 값은 필수입니다.")
  @DecimalMin(value = "-180.0", message = "남서쪽 경도는 -180.0 이상이어야 합니다.")
  @DecimalMax(value = "180.0", message = "남서쪽 경도는 180.0 이하이어야 합니다.")
  private Double southWestLon;

}
