package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query("SELECT a FROM Article a WHERE a.title LIKE %:title%")
  List<Article> findByTitleContaining(@Param("title") String title);

}
