package com.mallangs.domain.board.dto;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityDetailResponse {
    private final Long boardId;
    private final String categoryName;
    private final String title;
    private final String content;
    private final Nickname writerNickname;
    private final Long writerId;
    private final LocalDateTime createdAt;
    private final int viewCount;
    private final int commentCount;
    private final int likeCount;
    private final String imgUrl;
    private final BoardStatus status;

    public CommunityDetailResponse(Board board) {
        this.boardId = board.getBoardId();
        this.categoryName = board.getCategory().getName();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerNickname = board.getMember().getNickname();
        this.writerId = board.getMember().getMemberId();
        this.createdAt = board.getCreatedAt();
        this.viewCount = board.getViewCnt();
        this.commentCount = board.getCommentCnt();
        this.likeCount = board.getLikeCnt();
        this.imgUrl = board.getImgUrl();
        this.status = board.getBoardStatus();
    }
}
