package com.mallangs.domain.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "게시글 타입")
public enum BoardType {
    @Schema(description = "커뮤니티 게시글")
    COMMUNITY("커뮤니티 게시판"),
    @Schema(description = "실종신고-목격제보 게시글")
    SIGHTING("실종신고 게시판");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }
}
