package com.mallangs.domain.article.factory;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.LostCreateRequest;
import com.mallangs.domain.article.dto.request.SightingArticleCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.LostResponse;
import com.mallangs.domain.article.dto.response.SightingArticleResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.LostArticle;
import com.mallangs.domain.article.entity.SightingArticle;
import com.mallangs.domain.board.dto.request.SightingCreateRequest;
import com.mallangs.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class SightArticleFactory implements ArticleFactory {

  @Override
  public Article createArticle(Member member, ArticleCreateRequest request) {
    return SightingArticle.createSightingArticle(member, (SightingArticleCreateRequest) request);
  }

  @Override
  public SightingArticleResponse createResponse(Article article) {
    SightingArticle sightArticle = (SightingArticle) article;
    return new SightingArticleResponse(sightArticle);
  }

}