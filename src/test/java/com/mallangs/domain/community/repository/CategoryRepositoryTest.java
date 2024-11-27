package com.mallangs.domain.community.repository;

import com.mallangs.domain.community.entity.Category;
import com.mallangs.domain.community.entity.CategoryStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category saveCategory(String name, int order, int level, CategoryStatus status) {
        Category category = Category.builder()
                .name(name)
                .categoryOrder(order)
                .categoryLevel(level)
                .build();
        category.changeStatus(status);
        return categoryRepository.save(category);
    }

    @Test
    @DisplayName("활성화된 카테고리 목록 조회")
    void findAllActiveCategoriesTest() {
        // given
        saveCategory("일반게시판", 0, 0, CategoryStatus.ACTIVE);
        saveCategory("실종게시판", 1, 1, CategoryStatus.ACTIVE);
        saveCategory("목격게시판", 2, 1, CategoryStatus.INACTIVE);

        // when
        List<Category> categories = categoryRepository.findAllActiveCategories();

        // then
        assertThat(categories).hasSize(2)
                .extracting(Category::getName)
                .containsExactly("일반게시판", "실종게시판");
    }

    @Test
    @DisplayName("활성화된 카테고리 categoryId로 조회")
    void findActiveCategoryByIdTest() {
        // given
        Category category = saveCategory("일반게시판", 52, 0, CategoryStatus.ACTIVE);

        // when
        Optional<Category> foundCategory = categoryRepository.findActiveCategoryById(category.getCategoryId());

        // then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("일반게시판");
    }

    @Test
    @DisplayName("상태별 카테고리 목록 조회")
    void findAllByStatusOrderByCategoryOrder() {
        // given
        saveCategory("실종게시판", 1, 0, CategoryStatus.ACTIVE);
        saveCategory("목격게시판", 2, 1, CategoryStatus.INACTIVE);

        // when
        List<Category> activeCategories = categoryRepository.findAllByStatusOrderByCategoryOrder(CategoryStatus.ACTIVE);
        List<Category> inactiveCategories = categoryRepository.findAllByStatusOrderByCategoryOrder(CategoryStatus.INACTIVE);

        // then
        assertThat(activeCategories).hasSize(1)
                .extracting(Category::getName)
                .containsExactly("실종게시판");

        assertThat(inactiveCategories).hasSize(1)
                .extracting(Category::getName)
                .containsExactly("목격게시판");
    }

    @Test
    @DisplayName("이름으로 카테고리 검색")
    void findByNameContaining() {
        // given
        saveCategory("일반게시판", 0, 0, CategoryStatus.ACTIVE);
        saveCategory("실종게시판", 1, 0, CategoryStatus.INACTIVE);
        saveCategory("목격게시판", 2, 1, CategoryStatus.INACTIVE);
        saveCategory("정보게시판", 3, 1, CategoryStatus.INACTIVE);

        // when
        List<Category> categories = categoryRepository.findByNameContaining("일반");

        // then
        assertThat(categories).hasSize(1)
                .extracting(Category::getName)
                .containsExactly("일반게시판");
    }

    @Test
    @DisplayName("카테고리 상태별 개수 확인")
    void countByCategoryStatus() {
        // given
        saveCategory("일반게시판", 0, 0, CategoryStatus.ACTIVE);
        saveCategory("실종게시판", 1, 0, CategoryStatus.INACTIVE);
        saveCategory("목격게시판", 2, 1, CategoryStatus.INACTIVE);

        // when
        long activeCount = categoryRepository.countByCategoryStatus(CategoryStatus.ACTIVE);
        long inactiveCount = categoryRepository.countByCategoryStatus(CategoryStatus.INACTIVE);

        // then
        assertThat(activeCount).isEqualTo(1);
        assertThat(inactiveCount).isEqualTo(2);
    }
}