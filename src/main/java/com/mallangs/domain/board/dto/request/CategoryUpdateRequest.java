package com.mallangs.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    private String name;
    private String description;
    private int categoryOrder;
    private String categoryStatus;

    @Builder
    public CategoryUpdateRequest(String name, String description, int categoryOrder, String categoryStatus) {
        this.name = name;
        this.description = description;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }
}
