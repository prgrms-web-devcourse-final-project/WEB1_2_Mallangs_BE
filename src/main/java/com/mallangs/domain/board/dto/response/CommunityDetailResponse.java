package com.mallangs.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mallangs.domain.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityDetailResponse {
    private final Long boardId;
    private final String title;
    private final String content;
    private final String writer;
    private final String categoryName;
    private final int viewCnt;
    private final int commentCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    public CommunityDetailResponse(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember().getNickname().getValue();
        this.categoryName = board.getCategory().getName();
        this.viewCnt = board.getViewCnt();
        this.commentCnt = board.getCommentCnt();
        this.createdAt = board.getCreatedAt();
    }
}
