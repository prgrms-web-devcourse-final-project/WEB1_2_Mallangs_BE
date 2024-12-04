package com.mallangs.domain.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "게시글 상태")
public enum BoardStatus {
    @Schema(description = "임시 저장")
    DRAFT("임시 저장"),
    @Schema(description = "공개")
    PUBLISHED("공개"),
    @Schema(description = "숨김")
    HIDDEN("숨김");

    private final String description;

    BoardStatus(String description) {
        this.description = description;
    }

}
