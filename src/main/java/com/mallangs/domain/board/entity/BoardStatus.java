package com.mallangs.domain.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 상태")
public enum BoardStatus {
    @Schema(description = "임시 저장")
    DRAFT,
    @Schema(description = "공개")
    PUBLISHED,
    @Schema(description = "숨김")
    HIDDEN
}
