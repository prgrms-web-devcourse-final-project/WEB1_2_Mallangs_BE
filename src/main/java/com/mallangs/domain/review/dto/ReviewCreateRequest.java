package com.mallangs.domain.review.dto;

import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.review.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "평점을 입력해주세요")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    private Integer score;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 200, message = "내용은 200자 이내로 입력해주세요")
    private String content;

    @Size(max = 3 * 1024 * 1024, message = "이미지 크기는 3MB를 초과할 수 없습니다.")
    private String image;//이미지 사이즈 3MB 이하

    public Review toEntity(PlaceArticle placeArticle, Member member) {
        return Review.builder()
                .member(member)
                .placeArticle(placeArticle)
                .score(score)
                .content(content)
                .image(image)
                .build();
    }
}
