package com.mallangs.domain.board.controller;

import com.mallangs.domain.board.dto.request.CategoryCreateRequest;
import com.mallangs.domain.board.dto.request.CategoryUpdateRequest;
import com.mallangs.domain.board.dto.response.CategoryResponse;
import com.mallangs.domain.board.entity.CategoryStatus;
import com.mallangs.domain.board.service.CategoryService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "카테고리 API", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성 - 관리자 권한", description = "새로운 카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create("/api/category/" + response.getCategoryId())).body(response);
    }

    @Operation(summary = "카테고리 수정 - 관리자 권한", description = "기존 카테고리를 수정합니다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, request));
    }

    @Operation(summary = "카테고리 상태 변경 - 관리자 권한", description = "요청 형태: PATCH /api/categories/{카테고리ID}/status?status={상태}")
    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<Void> changeCategoryStatus(@PathVariable Long categoryId, @RequestBody CategoryStatus status) {
        categoryService.changeCategoryStatus(categoryId, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "카테고리 순서 변경 - 관리자 권한", description = "요청 형태: PATCH /api/category/{카테고리ID}/order?order={순서}")
    @PatchMapping("/{categoryId}/order")
    public ResponseEntity<Void> changeCategoryOrder(@PathVariable Long categoryId, @RequestParam(name = "order") int newOrder) {
        categoryService.changeCategoryOrder(categoryId, newOrder);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "카테고리 검색 - 관리자 권한", description = "요청 형태: GET /api/category/search?name={카테고리 이름}")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(
            @RequestParam String name,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boolean isAdmin = customMemberDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name, isAdmin));
    }

    @Operation(summary = "카테고리 전체 조회", description = "활성화된 모든 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }

    @Operation(summary = "특정 카테고리 조회", description = "ID로 특정 카테고리를 조회합니다.")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }
}
