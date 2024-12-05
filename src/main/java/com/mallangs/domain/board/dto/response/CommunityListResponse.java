package com.mallangs.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mallangs.domain.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityListResponse {
    private final Long boardId;
    private final String title;
    private final String writer;
    private final String categoryName;
    private final int viewCount;
    private final int commentCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    public CommunityListResponse(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.writer = board.getMember().getNickname().getValue();
        this.categoryName = board.getCategory().getName();
        this.viewCount = board.getViewCnt();
        this.commentCount = board.getCommentCnt();
        this.createdAt = board.getCreatedAt();
    }
}
