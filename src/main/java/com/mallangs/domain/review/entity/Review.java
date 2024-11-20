package com.mallangs.domain.review.entity;

import com.mallangs.domain.member.Member;
import com.mallangs.domain.place.entity.Place;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String image;

}
