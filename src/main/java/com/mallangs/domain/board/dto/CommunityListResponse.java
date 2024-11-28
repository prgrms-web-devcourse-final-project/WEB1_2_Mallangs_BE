package com.mallangs.domain.board.dto;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.member.entity.embadded.Nickname;

import java.time.LocalDateTime;

public class CommunityListResponse {

    private final Long boardId;
    private final String categoryName;
    private final String title;
    private final Nickname writerNickname;
    private final LocalDateTime createdAt;
    private final int viewCnt;
    private final int commentCnt;
    private final int likeCnt;

    public CommunityListResponse(Board board) {
        this.boardId = board.getBoardId();
        this.categoryName = board.getCategory().getName();
        this.title = board.getTitle();
        this.writerNickname = board.getMember().getNickname();
        this.createdAt = board.getCreatedAt();
        this.viewCnt = board.getViewCnt();
        this.commentCnt = board.getCommentCnt();
        this.likeCnt = board.getLikeCnt();
    }
}
