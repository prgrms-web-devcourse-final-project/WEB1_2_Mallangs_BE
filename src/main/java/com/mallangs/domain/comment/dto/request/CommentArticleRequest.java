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
public class CommentArticleRequest {
    @NotNull
    private Long memberId;

    @NotNull
    private Long articleId;

    @NotEmpty
    private String content;
}
