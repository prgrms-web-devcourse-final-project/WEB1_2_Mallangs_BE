package com.mallangs.domain.board.dto.response;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.member.entity.embadded.Nickname;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminBoardResponse {
    private Long boardId;
    private String title;
    private String writer;
    private String categoryName;
    private BoardType boardType;
    private BoardStatus boardStatus;
    private int viewCount;
    private int commentCount;
    private LocalDateTime createdAt;

    public static AdminBoardResponse from(Board board) {
        return new AdminBoardResponse(
                board.getBoardId(),
                board.getTitle(),
                board.getMember().getNickname().getValue(),
                board.getCategory().getName(),
                board.getBoardType(),
                board.getBoardStatus(),
                board.getViewCnt(),
                board.getCommentCnt(),
                board.getCreatedAt()
        );
    }
}