package com.mallangs.domain.review.dto;

import com.mallangs.domain.review.entity.ReviewStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewUpdateRequest {

    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    private Integer score;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 200, message = "내용은 200자 이내로 입력해주세요")
    private String content;

    @Size(max = 3 * 1024 * 1024, message = "이미지 크기는 3MB를 초과할 수 없습니다.")
    private String image;//이미지 사이즈 3MB 이하

    private ReviewStatus status;
}
