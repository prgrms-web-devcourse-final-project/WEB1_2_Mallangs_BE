package com.mallangs.domain.board.dto.request;

import com.mallangs.domain.board.entity.CategoryLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequest {

    private Long parentCategoryId;
    private String name;
    private String description;
    private CategoryLevel categoryLevel;
    private int categoryOrder;

    @Builder
    public CategoryCreateRequest(Long parentCategoryId, String name, String description, CategoryLevel categoryLevel, int categoryOrder) {
        this.parentCategoryId = parentCategoryId;
        this.name = name;
        this.description = description;
        this.categoryLevel = categoryLevel;
        this.categoryOrder = categoryOrder;
    }
}

