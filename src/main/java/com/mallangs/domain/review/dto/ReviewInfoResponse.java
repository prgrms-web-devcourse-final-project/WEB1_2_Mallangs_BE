package com.mallangs.domain.review.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mallangs.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ReviewInfoResponse {

    private Long reviewId;
    private Long articleId;
    private Long memberId;
    private Integer score;
    private String content;
    private String image;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    public ReviewInfoResponse (Review review) {
        this.reviewId = review.getReviewId();
        this.articleId= review.getPlaceArticle().getId();
        this.memberId = review.getMember().getMemberId();
        this.score = review.getScore();
        this.content = review.getContent();
        this.image = review.getImage();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}