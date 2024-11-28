package com.mallangs.domain.board.entity;

import lombok.Getter;

@Getter
public enum BoardType {
    COMMUNITY("커뷰니티 게시판"),
    SIGHTING("실종신고 게시판");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }
}
