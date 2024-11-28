package com.mallangs.domain.board.repository;

import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.entity.CategoryStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 사용자용 기능
    // 활성화된 모든 카테고리 조회
    @Query("""
        SELECT c FROM Category c WHERE c.categoryStatus = 'ACTIVE' ORDER BY c.categoryOrder ASC
        """)
    List<Category> findAllActiveCategories();

    // 활성화 된 카테고리 중 특정 카테고리 검색
    @Query("""
        SELECT c FROM Category c WHERE c.categoryId = :categoryId AND c.categoryStatus = 'ACTIVE'
        """)
    Optional<Category> findActiveCategoryById(@Param("categoryId") Long categoryId);

    // 관리자용 기능
    // 카테고리 별 목록 조회
    @Query("""
            SELECT c FROM Category c WHERE c.categoryStatus = :categoryStatus ORDER BY c.categoryOrder ASC
            """)
    List<Category> findAllByStatusOrderByCategoryOrder(@Param("status") CategoryStatus categoryStatus);

    // 카테고리 이름으로 검색
    List<Category> findByNameContaining(String name);

    // 카테고리 상태 별 개수 파악
    long countByCategoryStatus(CategoryStatus categoryStatus);
}
