package com.mallangs.domain.board.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class AdminBoardsResponse {
    private final Page<AdminBoardResponse> boards;
    private final BoardStatusCount statusCount;

    public AdminBoardsResponse(Page<AdminBoardResponse> boards, BoardStatusCount statusCount) {
        this.boards = boards;
        this.statusCount = statusCount;
    }
}