package com.mallangs.domain.review.entity;

import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id") //별도의 Place 키가 없고 아티클에 종속됨
    private PlaceArticle placeArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReviewStatus status= ReviewStatus.PUBLISHED;

    public void change(Integer score, String content, String image, ReviewStatus status) {
        this.score = score;
        this.content = content;
        this.image = image;
        this.status = status;
    }

}