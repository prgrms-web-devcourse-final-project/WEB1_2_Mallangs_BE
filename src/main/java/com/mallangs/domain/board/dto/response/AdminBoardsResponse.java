package com.mallangs.domain.board.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class AdminBoardsResponse {
    private List<AdminBoardResponse> boards;
    private BoardStatusCount statusCount;

    public AdminBoardsResponse(Page<AdminBoardResponse> boards, BoardStatusCount statusCount) {
        this.boards = boards.getContent();
        this.statusCount = statusCount;
    }
}
