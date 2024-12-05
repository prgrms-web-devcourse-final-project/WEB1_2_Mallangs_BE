package com.mallangs.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityUpdateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}