package com.mallangs.domain.article.entity;

import com.mallangs.domain.member.Member;
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
  @Column(name = "map_visibility", nullable = false)
  private MapVisibility mapVisibility;

  @Column(nullable = false, length = 100)
  private String title; // 장소인 경우 장소 이름

//  @Column(nullable = false)
//  private Double latitude;
//
//  @Column(nullable = false)
//  private Double longitude;

  @Column(nullable = false, columnDefinition = "POINT SRID 4326")
  private Point geography;

  @Column(length = 500)
  private String description;

  private String contact;

  @Column(columnDefinition = "TEXT")
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  public void applyChanges(Article updatedArticle) {
    if (updatedArticle.getMapVisibility() != null) {
      this.mapVisibility = updatedArticle.getMapVisibility();
    }
    if (updatedArticle.getTitle() != null) {
      this.title = updatedArticle.getTitle();
    }
    // 위도 경도 수정
    if (updatedArticle.getDescription() != null) {
      this.description = updatedArticle.getDescription();
    }
    if (updatedArticle.getContact() != null) {
      this.contact = updatedArticle.getContact();
    }
    if (updatedArticle.getImage() != null) {
      this.image = updatedArticle.getImage();
    }
  }
}
