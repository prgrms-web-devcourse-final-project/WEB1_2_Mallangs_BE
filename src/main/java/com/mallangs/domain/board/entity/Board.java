package com.mallangs.domain.board.entity;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(length = 200)
    private String address;

    private LocalDateTime sightedAt;

    private int viewCnt;

    private int commentCnt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus boardStatus;

    @Builder

    public Board(Member member, Category category, String title, String content, BigDecimal latitude,
                 BigDecimal longitude, String address, LocalDateTime sightedAt, BoardType boardType) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sightedAt = sightedAt;
        this.boardType = boardType;
        this.boardStatus = BoardStatus.PUBLISHED;
        this.viewCnt = 0;
        this.commentCnt = 0;
    }

    // 커뮤니티 게시글용 생성 메서드
    public static Board createCommunityBoard(Member member, Category category, String title, String content) {
        return Board.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .boardType(BoardType.COMMUNITY)
                .build();
    }

    // 실종신고-목격제보용 생성 메서드
    public static Board createSightingBoard(Member member, Category category,
                                            String title, String content, BigDecimal latitude, BigDecimal longitude,
                                            String address, LocalDateTime sightedAt) {
        return Board.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .sightedAt(sightedAt)
                .boardType(BoardType.SIGHTING)
                .build();
    }

    // 게시글 정보 수정
    public void change(String title, String content, BigDecimal latitude, BigDecimal longitude,
                       String address, LocalDateTime sightedAt) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sightedAt = sightedAt;
    }

    // 게시글 상태 변경
    public void changeStatus(BoardStatus status) {
        this.boardStatus = status;
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCnt++;
    }
}
