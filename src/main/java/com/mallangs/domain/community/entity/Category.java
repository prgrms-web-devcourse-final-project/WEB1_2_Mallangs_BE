package com.mallangs.domain.community.entity;

import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int categoryOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private int categoryStatus;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Community> communities = new ArrayList<>();

    public Category(String name, String description, int categoryOrder, int categoryStatus) {
        this.name = name;
        this.description = description;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }

    // 카테고리 수정
    public void changeCategory(String name, String description, int categoryOrder, int categoryStatus) {
        this.name = name;
        this.description = description;
        this.categoryOrder = categoryOrder;
        this.categoryStatus = categoryStatus;
    }

    // 카테고리 상태 변경
    public void changeStatus(int categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    // 카테고리 순서 변경
    public void changeOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }
}
>>>>>>> 39b9640dc7bf03c3212f65d70f7375c534725c58
