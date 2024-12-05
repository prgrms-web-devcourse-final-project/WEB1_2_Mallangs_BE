package com.mallangs.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SightingUpdateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotBlank
    private String address;

    @NotNull
    private LocalDateTime sightedAt;
}

