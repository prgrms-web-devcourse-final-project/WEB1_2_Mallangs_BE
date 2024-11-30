package com.mallangs.domain.article.factory;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.LostCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.LostResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.LostArticle;
import com.mallangs.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class LostArticleFactory implements ArticleFactory {

  @Override
  public Article createArticle(Member member, ArticleCreateRequest request) {
    return LostArticle.createLostArticle(member, (LostCreateRequest) request);
  }

  @Override
  public ArticleResponse createResponse(Article article) {
    LostArticle lostArticle = (LostArticle) article;
    return new LostResponse(lostArticle);
  }
}