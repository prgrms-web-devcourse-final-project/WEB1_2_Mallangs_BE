package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.ArticleType;
import com.mallangs.domain.article.entity.CaseStatus;
import java.util.List;
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
  // article 에서 map visible // jpql 은 join 필요 없음
  @Query("SELECT l FROM LostArticle l WHERE l.lostStatus = :lostStatus AND l.mapVisibility = 'VISIBLE'")
  List<Article> findLostArticles(@Param("lostStatus") CaseStatus lostStatus);

  // 관리자 목록 조회
  // 다양한 타입 설정

  // 장소 // 사용자 등록 위치 구분
  @Query("SELECT p FROM PlaceArticle p WHERE p.isPublicData = :isPublicData")
  Page<Article> findPlaceArticlesByType(Pageable pageable, boolean isPublicData);
  
  // 장소 중에 소분류
  @Query("SELECT p FROM PlaceArticle p WHERE p.isPublicData = :isPublicData  AND p.category = :placeCategory")
  Page<Article> findPlaceArticlesByCategory(Pageable pageable, boolean isPublicData,
      String placeCategory);

  // 멤버 개인 글타래 목록 조회
  // 논리 삭제 된 것 제외
  @Query("SELECT a FROM Article a WHERE a.member.memberId = :memberId AND a.isDeleted = false")
  List<Article> findByMemberId(Long memberId);

  // 모든 사용자
  // 검색
  @Query("SELECT a FROM Article a WHERE (a.title LIKE %:title% OR a.description LIKE %:description%) AND a.mapVisibility = 'VISIBLE'")
  List<Article> findByTitleContainingOrDescriptionContainingAndMapVisibility(
      @Param("title") String title,
      @Param("description") String description);

  // 모든 사용자
  // 모든 목격글타래 조회
  // article 에서 map visible // jpql 은 join 필요 없음
  @Query("SELECT s FROM SightingArticle s WHERE s.lostArticleId = :lostSightId AND s.mapVisibility = 'VISIBLE'")
  List<Article> findSightingArticles(@Param("lostSightId") Long lostSightId);

}
