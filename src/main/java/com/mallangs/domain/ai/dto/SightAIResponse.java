package com.mallangs.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // DTO에 정의되지 않은 필드는 무시
public class SightAIResponse {

    @JsonProperty("sightArticleId")
    private Long sightArticleId;

    @JsonProperty("percentage")
    private Double percentage;

    @JsonProperty("findSpot")
    private String findSpot;

    @JsonProperty("sightedAt")
    private LocalDate sightedAt;

    @JsonProperty("breed")
    private String breed;

    @JsonProperty("color")
    private String color;

    @JsonProperty("gender")
    private String gender;

}
