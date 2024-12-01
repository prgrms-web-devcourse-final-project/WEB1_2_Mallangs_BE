package com.mallangs.domain.board.dto.response;

import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.entity.CategoryLevel;
import com.mallangs.domain.board.entity.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponse {

    private final Long categoryId;
    private final Long parentCategoryId;
    private final String name;
    private final String description;
    private final CategoryLevel categoryLevel;
    private final int categoryOrder;
    private final CategoryStatus categoryStatus;
    private final List<CategoryResponse> children;

    @Builder
    public CategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.parentCategoryId = category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null;
        this.name = category.getName();
        this.description = category.getDescription();
        this.categoryLevel = category.getCategoryLevel();
        this.categoryOrder = category.getCategoryOrder();
        this.categoryStatus = category.getCategoryStatus();
        this.children = category.getChildrenCategories().stream().map(CategoryResponse::new).collect(Collectors.toList());
    }

}
