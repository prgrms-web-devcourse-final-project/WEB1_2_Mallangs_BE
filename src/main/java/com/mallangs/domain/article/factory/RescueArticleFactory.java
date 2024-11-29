package com.mallangs.domain.article.factory;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.request.RescueCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.RescueArticle;
import com.mallangs.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class RescueArticleFactory implements ArticleFactory {

  @Override
  public Article createArticle(Member member, ArticleCreateRequest request) {
    return RescueArticle.createRescueArticle(member, (RescueCreateRequest) request);
  }

  @Override
  public ArticleResponse createResponse(Article article) {
    RescueArticle rescueArticle = (RescueArticle) article;
    return new ArticleResponse(rescueArticle);
  }
}
