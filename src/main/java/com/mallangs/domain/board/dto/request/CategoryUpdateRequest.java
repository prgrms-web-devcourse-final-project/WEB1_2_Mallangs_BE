package com.mallangs.domain.board.dto.request;

import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.entity.CategoryLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    private Long parentCategory;
    private String name;
    private String description;
    private CategoryLevel categoryLevel;
    private int categoryOrder;
    @Schema(description = "카테고리 상태", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    private String categoryStatus;

    @Builder
    public CategoryUpdateRequest(Long parentCategory, String name, String description, CategoryLevel categoryLevel,
                                 int categoryOrder, String categoryStatus) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.description = description;
        this.categoryLevel = categoryLevel;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }
}
