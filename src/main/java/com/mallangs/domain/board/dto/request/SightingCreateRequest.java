package com.mallangs.domain.board.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SightingCreateRequest {
    @NotNull(message = "카테고리를 선택하세요.")
    private Long categoryId;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotNull(message = "위도를 입력하세요.")
    @Digits(integer = 2, fraction = 8)
    private BigDecimal latitude;

    @NotNull(message = "경도를 입력하세요.")
    @Digits(integer = 3, fraction = 8)
    private BigDecimal longitude;

    @Size(max = 200)
    private String address;

    private LocalDateTime sightedAt;

    private String imgUrl;

}
