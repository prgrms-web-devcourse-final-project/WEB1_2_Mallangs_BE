package com.mallangs.domain.board.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SightingUpdateRequest {

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @Digits(integer = 2, fraction = 8)
    private BigDecimal latitude;

    @Digits(integer = 3, fraction = 8)
    private BigDecimal longitude;

    @Size(max = 200)
    private String address;

    private LocalDateTime sightedAt;

    private String imgUrl;
}
