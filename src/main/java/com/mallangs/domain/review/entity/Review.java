package com.mallangs.domain.review.entity;

import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.place.entity.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

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

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String image;

}