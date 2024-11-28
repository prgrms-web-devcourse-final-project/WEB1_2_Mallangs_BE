package com.mallangs.domain.board.dto;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminBoardResponse {
    private final Long boardId;
    private final BoardType boardType;
    private final String categoryName;
    private final String title;
    private final Nickname writerNickname;
    private final LocalDateTime createdAt;
    private final BoardStatus boardStatus;
    private final int viewCount;
    private final int commentCount;
    private final int likeCount;

    public AdminBoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.boardType = board.getBoardType();
        this.categoryName = board.getCategory().getName();
        this.title = board.getTitle();
        this.writerNickname = board.getMember().getNickname();
        this.createdAt = board.getCreatedAt();
        this.boardStatus = board.getBoardStatus();
        this.viewCount = board.getViewCnt();
        this.commentCount = board.getCommentCnt();
        this.likeCount = board.getLikeCnt();
    }

    public static AdminBoardResponse from(Board board) {
        return new AdminBoardResponse(board);
    }
}
