package com.mallangs.domain.community.entity;

import com.mallangs.domain.member.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long board_id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @Enumerated(EnumType.STRING)
//    @JoinColumn(name = "category_id")
//    private Category category;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

//    public Community(Member member, Category category, String title, String content, String location,
//                     Double latitude, Double longitude) {
//        this.member = member;
//        this.category = category;
//        this.title = title;
//        this.content = content;
//        this.location = location;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    public void update(String title, String content, String location, Double latitude, Double longitude) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
