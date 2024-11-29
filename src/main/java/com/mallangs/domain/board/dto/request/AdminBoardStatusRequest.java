package com.mallangs.domain.board.dto.request;

import com.mallangs.domain.board.entity.BoardStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AdminBoardStatusRequest {
    @NotEmpty(message = "게시글 ID 목록은 필수입니다.")
    private List<Long> boardIds;

    @NotNull(message = "변경할 상태값은 필수입니다.")
    private BoardStatus status;
}
