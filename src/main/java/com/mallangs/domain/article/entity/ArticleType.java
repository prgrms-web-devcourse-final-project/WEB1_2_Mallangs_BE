package com.mallangs.domain.article.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleType {

  @JsonProperty("lost")
  LOST("lost"),

  @JsonProperty("rescue")
  RESCUE("rescue"),

  @JsonProperty("place")
  PLACE("place");

  private final String description;
}
