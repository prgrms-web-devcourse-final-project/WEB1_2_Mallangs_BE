package com.mallangs.domain.board.dto.response;

import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.entity.CategoryLevel;
import com.mallangs.domain.board.entity.CategoryStatus;
import lombok.Getter;

@Getter
public class AdminCategoryResponse {
    private final Long categoryId;
    private final String name;
    private final String description;
    private final CategoryLevel categoryLevel;
    private final int categoryOrder;
    private final CategoryStatus categoryStatus;
    private final String parentCategoryName;
    private final int childCount;

    public AdminCategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.categoryLevel = category.getCategoryLevel();
        this.categoryOrder = category.getCategoryOrder();
        this.categoryStatus = category.getCategoryStatus();
        this.parentCategoryName = category.getParentCategory() != null ?
                category.getParentCategory().getName() : null;
        this.childCount = category.getChildrenCategories().size();
    }
}