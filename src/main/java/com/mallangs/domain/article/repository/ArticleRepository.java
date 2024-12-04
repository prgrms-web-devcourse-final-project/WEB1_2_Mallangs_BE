package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.ArticleType;
import com.mallangs.domain.article.entity.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 관리자
  // 글타래 타입 별 조회
  @Query("SELECT a FROM Article a WHERE a.articleType = :articleType")
  Page<Article> findByArticleType(Pageable pageable, ArticleType articleType);

  // 모든 사용자
  // 실종 게시판 별도 확인
  // article 에서 map visible
  @Query("SELECT l FROM LostArticle l JOIN Article a ON l.id = a.id WHERE l.lostStatus = :lostStatus AND a.mapVisibility = 'VISIBLE'")
  Page<Article> findLostArticles(Pageable pageable, CaseStatus lostStatus);

  // 관리자 목록 조회
  // 다양한 타입 설정
  // 장소 중에 소분류
  @Query("SELECT p FROM PlaceArticle p WHERE p.isPublicData = :isPublicData  AND p.category = :placeCategory")
  Page<Article> findPlaceArticlesByCategory(Pageable pageable, boolean isPublicData,
      String placeCategory);

  // 멤버 개인 글타래 목록 조회
  // 논리 삭제 된 것 제외
  @Query("SELECT a FROM Article a WHERE a.member.memberId = :memberId AND a.isDeleted = false")
  Page<Article> findByMemberId(Pageable pageable, Long memberId);

  // 모든 사용자
  // 검색
  @Query("SELECT a FROM Article a WHERE (a.title LIKE %:title% OR a.description LIKE %:description%) AND a.mapVisibility = 'VISIBLE'")
  Page<Article> findByTitleContainingOrDescriptionContainingAndMapVisibility(
      @Param("title") String title,
      @Param("description") String description,
      Pageable pageable);
  

}
