package com.mallangs.domain.article.repository;

import com.mallangs.domain.article.entity.PlaceArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceArticleRepository extends JpaRepository<PlaceArticle, Long> {
}
