package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.PlaceArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceArticleRepository extends JpaRepository<PlaceArticle, Long> {
}
