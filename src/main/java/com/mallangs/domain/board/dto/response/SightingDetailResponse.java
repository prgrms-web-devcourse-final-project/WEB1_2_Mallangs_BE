package com.mallangs.domain.board.dto.response;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SightingDetailResponse {
    private final Long boardId;
    private final String categoryName;
    private final String title;
    private final String content;
    private final Nickname writerNickname;
    private final Long writerId;
    private final LocalDateTime createdAt;
    private final LocalDateTime sightedAt;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String address;
    private final Long imageId;
    private final int viewCount;
    private final int commentCount;
    private final BoardStatus boardStatus;

    public SightingDetailResponse(Board board) {
        this.boardId = board.getBoardId();
        this.categoryName = board.getCategory().getName();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerNickname = board.getMember().getNickname();
        this.writerId = board.getMember().getMemberId();
        this.createdAt = board.getCreatedAt();
        this.sightedAt = board.getSightedAt();
        this.latitude = board.getLatitude();
        this.longitude = board.getLongitude();
        this.address = board.getAddress();
        this.imageId = board.getImageId();
        this.viewCount = board.getViewCnt();
        this.commentCount = board.getCommentCnt();
        this.boardStatus = board.getBoardStatus();
    }
}
