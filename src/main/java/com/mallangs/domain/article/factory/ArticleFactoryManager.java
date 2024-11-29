package com.mallangs.domain.article.factory;

import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleFactoryManager {

  private final Map<String, ArticleFactory> articleFactoryMap;

  @Autowired
  public ArticleFactoryManager(List<ArticleFactory> articleFactories) {
    articleFactoryMap = articleFactories.stream()
        .collect(
            Collectors.toMap(
                factory -> factory.getClass().getSimpleName().replace("ArticleFactory", "")
                    .toLowerCase(),
                factory -> factory));
    System.out.println("articleFactoryMap size: " + articleFactoryMap.size());
  }

  public ArticleFactory getFactory(String articleType) {

    ArticleFactory factory = articleFactoryMap.get(articleType.toLowerCase());
    if (factory == null) {
      throw new MallangsCustomException(ErrorCode.ARTICLE_TYPE_NOT_FOUND);
    }
    return factory;
  }
}
