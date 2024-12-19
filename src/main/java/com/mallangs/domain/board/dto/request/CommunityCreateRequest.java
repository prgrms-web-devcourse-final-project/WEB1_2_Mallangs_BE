package com.mallangs.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityCreateRequest {
    @NotNull
    private Long categoryId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
