package com.mallangs.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBoardRequest {
    @NotNull
    private Long memberId;

    @NotNull
    private Long boardId;

    @NotEmpty
    private String content;
}
