package com.mallangs.domain.article.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlePageResponse {

  private List<ArticleResponse> articles;
  private long totalElements;
  private int totalPages;
  private int currentPage;
  private int pageSize;


}
