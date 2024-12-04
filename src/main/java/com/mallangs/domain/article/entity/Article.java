package com.mallangs.domain.article.entity;

import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "article_type") // db 에 존재
@Getter
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ArticleType articleType;

  @Builder.Default
  @Column(name = "isDeleted", nullable = false)
  private Boolean isDeleted = false;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "map_visibility", nullable = false)
  private MapVisibility mapVisibility = MapVisibility.VISIBLE; // 지도 표시 여부

  @Enumerated(EnumType.STRING)
  @Column(name = "article_status", nullable = false)
  private BoardStatus articleStatus; // 게시 상태

  @Column(nullable = false, length = 100)
  private String title; // 장소인 경우 장소 이름

  @Column(nullable = false, columnDefinition = "POINT SRID 4326")
  private Point geography;

  @Column(length = 500)
  private String description;

  @Column(columnDefinition = "TEXT")
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = true) // member 삭제 후에도 글은 남아있음
  private Member member;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // 논리 삭제를 위한 메서드
  public void deactivate() {
    this.mapVisibility = MapVisibility.HIDDEN;
    this.isDeleted = true;
  }

  // 글 상태에 따라 지도에서 숨기기
  public void hideInMap() {
    if (this.articleStatus != BoardStatus.PUBLISHED) {
      this.mapVisibility = MapVisibility.HIDDEN;
    }
  }

  public void applyChanges(Article updatedArticle) {
    if (updatedArticle.getArticleType() != null) {
      this.articleType = updatedArticle.getArticleType();
    }
    if (updatedArticle.getArticleStatus() != null) {
      this.articleStatus = updatedArticle.getArticleStatus();
    }
    if (updatedArticle.getTitle() != null) {
      this.title = updatedArticle.getTitle();
    }
    if (updatedArticle.getGeography() != null) {
      this.geography = updatedArticle.getGeography();
    }
    if (updatedArticle.getDescription() != null) {
      this.description = updatedArticle.getDescription();
    }
    if (updatedArticle.getImage() != null) {
      this.image = updatedArticle.getImage();
    }
  }
}
