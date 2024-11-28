package com.mallangs.domain.board.entity;

import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private final List<Category> childrenCategory = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    @Min(0)
    @Max(2)
    private int categoryLevel; // 0: 최상위 카테고리, 1: 1차 분류, 2: 2차 분류

    @Column(nullable = false)
    private int categoryOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryStatus categoryStatus;

    @Builder
    public Category(Category parentCategory, String name, String description, int categoryLevel,
                    int categoryOrder) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.description = description;
        this.categoryLevel = categoryLevel;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = CategoryStatus.ACTIVE;
    }

    // 카테고리 수정
    public void changeCategory(String name, String description, int categoryOrder, CategoryStatus categoryStatus) {
        this.name = name;
        this.description = description;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }

    // 카테고리 상태 변경
    public void changeStatus(CategoryStatus categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    // 카테고리 순서 변경
    public void changeOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

}
