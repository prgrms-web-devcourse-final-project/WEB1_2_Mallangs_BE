package com.mallangs.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 200, message = "내용은 200자 이내로 입력해주세요")
    private String content;
}
