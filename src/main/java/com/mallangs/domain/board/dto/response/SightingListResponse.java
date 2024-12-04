package com.mallangs.domain.board.dto.response;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SightingListResponse {

    private final Long boardId;
    private final String categoryName;
    private final String title;
    private final Nickname writerNickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime sightedAt;
    private final String address;
    private final Long imageId;
    private final int viewCount;
    private final int commentCount;

    public SightingListResponse(Board board) {
        this.boardId = board.getBoardId();
        this.categoryName = board.getCategory().getName();
        this.title = board.getTitle();
        this.writerNickname = board.getMember().getNickname();
        this.createdAt = board.getCreatedAt();
        this.sightedAt = board.getSightedAt();
        this.address = board.getAddress();
        this.imageId = board.getImageId();
        this.viewCount = board.getViewCnt();
        this.commentCount = board.getCommentCnt();
    }
}