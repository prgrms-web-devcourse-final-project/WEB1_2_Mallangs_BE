package com.mallangs.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // DTO에 정의되지 않은 필드는 무시
public class AiResponseDTO {
    @JsonProperty("response")
    private String response;

    @JsonProperty("details")
    private String details;
}
