package com.mallangs.domain.board.controller;

import com.mallangs.domain.board.dto.request.CategoryCreateRequest;
import com.mallangs.domain.board.dto.request.CategoryUpdateRequest;
import com.mallangs.domain.board.dto.response.CategoryResponse;
import com.mallangs.domain.board.entity.CategoryStatus;
import com.mallangs.domain.board.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info("=== Category creation request: {}", request);
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create("/api/category/" + response.getCategoryId())).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 상태 변경", description = "카테고리의 상태를 변경합니다.")
    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<Void> changeCategoryStatus(@PathVariable Long categoryId, @RequestParam CategoryStatus status) {
        categoryService.changeCategoryStatus(categoryId, status);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 순서 변경", description = "카테고리의 표시 순서를 변경합니다.")
    @PatchMapping("/{categoryId}/order")
    public ResponseEntity<Void> changeCategoryOrder(@PathVariable Long categoryId, @RequestParam int newOrder) {
        categoryService.changeCategoryOrder(categoryId, newOrder);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "카테고리 검색", description = "카테고리를 이름으로 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategories(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
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
