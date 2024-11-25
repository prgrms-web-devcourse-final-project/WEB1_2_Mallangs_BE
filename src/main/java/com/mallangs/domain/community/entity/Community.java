package com.mallangs.domain.community.entity;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String location;

    private String imgUrl;

    private int viewCnt;

    private int commentCnt;

    private int likeCnt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityStatus communityStatus;

    @Builder
    public Community(Member member, Category category, String title, String content,
                     String location, String imgUrl) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.location = location;
        this.imgUrl = imgUrl;
        this.communityStatus = CommunityStatus.PUBLISHED;
        this.viewCnt = 0;
        this.commentCnt = 0;
        this.likeCnt = 0;
    }

    // 게시글 정보 수정
    public void change(String title, String content, String location, String imgUrl) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.imgUrl = imgUrl;
    }
}
