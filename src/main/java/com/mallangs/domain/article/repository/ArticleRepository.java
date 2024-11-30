package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 글타래 타입 별 조회
  @Query("SELECT a FROM Article a WHERE TYPE(a) = :articleType")
  List<Article> findByArticleType(String articleType);


  // 글타래 종류 별 조회
//  List<Article> findArticleListByArticleType();

  // 멤버 개인 글타래 목록 조회
  @Query("SELECT a FROM Article a WHERE a.member.memberId = :memberId")
  Page<Article> findByMemberId(Pageable pageable, Long memberId);
//  List<Article> findArticleListByMember(Long memberId);

  // 위치 기준 검색
  //

  // 제목 검색
//  @Query("SELECT a FROM Article a WHERE a.title LIKE %:title%")
//  List<Article> findByTitleContaining(@Param("title") String title);

}
