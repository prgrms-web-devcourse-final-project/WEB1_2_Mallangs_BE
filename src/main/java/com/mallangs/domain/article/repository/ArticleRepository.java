package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.ArticleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 실종 게시판 별도 확인
  // 글타래 타입 별 조회
  @Query("SELECT a FROM Article a WHERE a.articleType = :articleType")
  Page<Article> findByArticleType(Pageable pageable, ArticleType articleType);

  // 다양한 타입 설정 -> 관리자
  // 장소 중에 소분류
  @Query("SELECT p FROM PlaceArticle p WHERE p.category = :placeCategory")
  Page<Article> findPlaceArticlesByCategory(Pageable pageable, String placeCategory);

  // 멤버 개인 글타래 목록 조회
  @Query("SELECT a FROM Article a WHERE a.member.memberId = :memberId")
  Page<Article> findByMemberId(Pageable pageable, Long memberId);

  // 검색
  @Query("SELECT a FROM Article a WHERE (a.title LIKE %:title% OR a.description LIKE %:description%) AND a.mapVisibility = 'VISIBLE'")
  Page<Article> findByTitleContainingOrDescriptionContainingAndMapVisibility(
      @Param("title") String title,
      @Param("description") String description,
      Pageable pageable);

//  Page<Article> findByTitleContainingOrDescriptionContaining(
//      String title, String description, Pageable pageable);


}
