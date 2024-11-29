package com.mallangs.domain.review.dto;

import com.mallangs.domain.review.entity.ReviewStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {

    private Integer score;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 200, message = "내용은 200자 이내로 입력해주세요")
    private String content;

    @Size(max = 3 * 1024 * 1024, message = "이미지 크기는 3MB를 초과할 수 없습니다.")
    private String image;//이미지 사이즈 3MB 이하

    private ReviewStatus status;
}