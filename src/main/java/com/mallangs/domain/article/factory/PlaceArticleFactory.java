package com.mallangs.domain.article.factory;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.PlaceCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.PlaceResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class PlaceArticleFactory implements ArticleFactory {

  @Override
  public Article createArticle(Member member, ArticleCreateRequest request) {
    return PlaceArticle.createPlaceArticle(member, (PlaceCreateRequest) request);
  }

  @Override
  public ArticleResponse createResponse(Article article) {
    PlaceArticle placeArticle = (PlaceArticle) article;
    return new PlaceResponse(placeArticle);
  }
}