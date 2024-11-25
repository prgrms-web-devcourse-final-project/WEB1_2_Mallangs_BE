package com.mallangs.domain.article.entity;

import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "article_type")
@Getter
public abstract class Article extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "map_visibility", nullable = false)
  private MapVisibility mapVisibility;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Column(length = 500)
  private String description;

  private String contact;

  @Column(columnDefinition = "TEXT")
  private String image;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "member_id")
//  private Member member;


}
