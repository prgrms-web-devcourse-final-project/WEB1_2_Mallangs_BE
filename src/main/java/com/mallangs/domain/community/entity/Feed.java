package com.mallangs.domain.community.entity;

import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feed_id;

    @Column(nullable = false)
    private Long member_id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Integer likeCnt;

    private Integer commentCnt;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<like> likes = new ArrayList<>();

    private Feed(Long member_id, String content) {}

}
