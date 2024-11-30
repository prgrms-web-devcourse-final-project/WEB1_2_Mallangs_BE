package com.mallangs.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    private String name;
    private String description;
    private int categoryLevel;
    private int categoryOrder;
    private String categoryStatus;

    @Builder
    public CategoryUpdateRequest(String name, String description, int categoryLevel, int categoryOrder, String categoryStatus) {
        this.name = name;
        this.description = description;
        this.categoryLevel = categoryLevel;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }
}
