package com.mallangs.domain.article.factory;

import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArticleFactoryManager {

  private final Map<String, ArticleFactory> articleFactoryMap;

  @Autowired
  public ArticleFactoryManager(List<ArticleFactory> articleFactories) {
    articleFactories.forEach(factory ->
        log.info("Factory created: {}", factory.getClass().getSimpleName())
    );

    articleFactoryMap = articleFactories.stream()
        .collect(
            Collectors.toMap(
                factory -> factory.getClass().getSimpleName().replace("ArticleFactory", "")
                    .toLowerCase(),
                factory -> factory));

    log.info("articleFactoryMap size: {}", articleFactoryMap.size());

    articleFactoryMap.forEach((key, value) ->
        log.info("Key: {}, Value: {}", key, value.getClass().getSimpleName())
    );
  }

  public ArticleFactory getFactory(String articleType) {

    ArticleFactory factory = articleFactoryMap.get(articleType.toLowerCase());
    if (factory == null) {
      throw new MallangsCustomException(ErrorCode.ARTICLE_TYPE_NOT_FOUND);
    }
    return factory;
  }
}
