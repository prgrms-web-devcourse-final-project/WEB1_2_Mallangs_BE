package com.mallangs.domain.board.service;

import com.mallangs.domain.board.dto.request.CategoryCreateRequest;
import com.mallangs.domain.board.dto.request.CategoryUpdateRequest;
import com.mallangs.domain.board.dto.response.CategoryResponse;
import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.entity.CategoryStatus;
import com.mallangs.domain.board.repository.CategoryRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 활성화 상태의 카테고리 조회
    public List<CategoryResponse> getAllActiveCategories() {
        List<Category> categories = categoryRepository.findAllActiveCategories();
        return categories.stream()
                .filter(category -> category.getCategoryLevel() == 0)
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 카테고리 조회
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findActiveCategoryById(categoryId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryResponse(category);
    }

    // 카테고리 생성
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        log.info("=== Service creating category: {}", request);
        Category parentCategory = null;
        if (request.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARENT_CATEGORY_NOT_FOUND));
        }

        Category category = Category.builder()
                .parentCategory(parentCategory)
                .name(request.getName())
                .description(request.getDescription())
                .categoryLevel(request.getCategoryLevel())
                .categoryOrder(request.getCategoryOrder())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory);
    }

    // 카테고리 수정
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));

        category.changeCategory(
                request.getName(),
                request.getDescription(),
                request.getCategoryOrder(),
                CategoryStatus.valueOf(request.getCategoryStatus())
        );
        return new CategoryResponse(category);
    }

    // 카테고리 상태 변경
    @Transactional
    public void changeCategoryStatus(Long categoryId, CategoryStatus status) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));
        category.changeStatus(status);
    }

    // 카테고리 순서 변경
    @Transactional
    public void changeCategoryOrder(Long categoryId, int newOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));
        category.changeOrder(newOrder);
    }

    // 카테고리 이름으로 검색
    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContaining(name);
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }
}
