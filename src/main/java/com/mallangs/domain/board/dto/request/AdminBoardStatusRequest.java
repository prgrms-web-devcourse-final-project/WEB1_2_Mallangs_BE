package com.mallangs.domain.board.dto.request;

import com.mallangs.domain.board.entity.BoardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AdminBoardStatusRequest {
    @NotNull
    private List<Long> boardIds;

    @NotNull
    private BoardStatus status;
}
