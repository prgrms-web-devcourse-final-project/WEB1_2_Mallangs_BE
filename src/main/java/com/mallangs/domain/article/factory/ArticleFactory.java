package com.mallangs.domain.article.factory;

import com.mallangs.domain.article.dto.request.ArticleCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.member.entity.Member;

public interface ArticleFactory {

  Article createArticle(Member member, ArticleCreateRequest articleCreateRequest);

  ArticleResponse createResponse(Article article);

}
