package com.mallangs.domain.board.dto.response;

import lombok.Getter;

@Getter
public class BoardStatusCount {
    private final long totalCount;
    private final long publicCount;
    private final long hiddenCount;
    private final long draftCount;

    public BoardStatusCount(long totalCount, long publicCount, long hiddenCount, long draftCount) {
        this.totalCount = totalCount;
        this.publicCount = publicCount;
        this.hiddenCount = hiddenCount;
        this.draftCount = draftCount;
    }
}
