package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.ArticleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 글타래 타입 별 조회
  @Query("SELECT a FROM Article a WHERE a.articleType = :articleType")
  Page<Article> findByArticleType(Pageable pageable, ArticleType articleType);

  // 멤버 개인 글타래 목록 조회
  @Query("SELECT a FROM Article a WHERE a.member.memberId = :memberId")
  Page<Article> findByMemberId(Pageable pageable, Long memberId);

  // 검색
  Page<Article> findByTitleContainingOrDescriptionContaining(
      String title, String description, Pageable pageable);


}
