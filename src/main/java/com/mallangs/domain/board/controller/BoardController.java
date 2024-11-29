package com.mallangs.domain.board.controller;

import com.mallangs.domain.board.dto.request.*;
import com.mallangs.domain.board.dto.response.*;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.board.service.BoardService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "커뮤니티 & 실종신고-목격제보 API", description = "커뮤니티/실종신고-목격제보 관련 API")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    // 커뮤니티 관련
    @Operation(summary = "커뮤니티 게시글 작성", description = "커뮤니티 게시판에 글을 작성합니다.")
    @PostMapping("/community")
    public ResponseEntity<Long> createCommunity(@Valid @RequestBody CommunityCreateRequest request,
                                                @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Long boardId = boardService.createCommunityBoard(request, customMemberDetails.getMemberId());
        return ResponseEntity.created(URI.create("/api/board/community/" + boardId)).body(boardId);
    }

    @Operation(summary = "커뮤니티 카테고리별 게시글 목록 조회", description = "특정 카테고리의 커뮤니티 게시글 목록을 조회합니다.")
    @GetMapping("/community/category/{categoryId}")
    public ResponseEntity<Page<CommunityListResponse>> getCommunityPostByCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
            @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoardsByCategory(categoryId, pageable));
    }

    @Operation(summary = "커뮤니티 게시글 상세 조회", description = "특정 커뮤니티 게시글의 상세 내용을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @GetMapping("/community/{boardId}")
    public ResponseEntity<CommunityDetailResponse> getCommunityPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoard(boardId));
    }

    @Operation(summary = "커뮤니티 게시글 수정", description = "기존 커뮤니티 게시글을 수정합니다.")
    @PutMapping("/community/{boardId}")
    public ResponseEntity<Void> updateCommunityPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Valid @RequestBody CommunityUpdateRequest request,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.updateCommunityBoard(boardId, request, customMemberDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

    // 실종신고-목격제보 관련
    @Operation(summary = "실종신고-목격제보 게시글 작성", description = "새로운 실종신고-목격제보 게시글을 작성합니다.")
    @PostMapping("/sighting")
    public ResponseEntity<Long> createSightingPost(
            @Valid @RequestBody SightingCreateRequest request,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Long boardId = boardService.createSightingBoard(request, customMemberDetails.getMemberId());
        return ResponseEntity.created(URI.create("/api/board/sighting/" + boardId)).body(boardId);
    }

    @Operation(summary = "실종신고-목격제보 카테고리별 목격 게시글 목록 조회", description = "특정 카테고리의 실종신고-목격제보 게시글 목록을 조회합니다.")
    @GetMapping("/sighting/category/{categoryId}")
    public ResponseEntity<Page<SightingListResponse>> getSightingPostsByCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
            @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.getSightingBoardsByCategory(categoryId, pageable));
    }

    @Operation(summary = "실종신고-목격제보 게시글 상세 조회", description = "특정 실종신고-목격제보 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/sighting/{boardId}")
    public ResponseEntity<SightingDetailResponse> getSightingPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId
    ) {
        return ResponseEntity.ok(boardService.getSightingBoard(boardId));
    }

    @Operation(summary = "실종신고-목격제보 게시글 수정", description = "기존 실종신고-목격제보 게시글을 수정합니다.")
    @PutMapping("/sighting/{boardId}")
    public ResponseEntity<Void> updateSightingPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Valid @RequestBody SightingUpdateRequest request,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.updateSightingBoard(boardId, request, customMemberDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제(공통)
    @Operation(summary = "게시글 삭제", description = "커뮤니티 또는 실종신고-목격제보 게시물의 상태를 HIDDEN으로 변경합니다.")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Parameter(description = "게시글 타입") @RequestParam BoardType boardType,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.deleteBoard(boardId, customMemberDetails.getMemberId(), boardType);
        return ResponseEntity.noContent().build();
    }

    // 관리자 기능
    @Operation(summary = "관리자용 게시글 검색", description = "관리자가 게시글을 검색하고 필터링합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/search")
    public ResponseEntity<AdminBoardsResponse> searchPosts(
            @Parameter(description = "게시글 상태") @RequestParam(required = false) BoardStatus status,
            @Parameter(description = "카테고리 ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 10) Pageable pageable
    ) {
        if (status != null && categoryId != null) {
            return ResponseEntity.ok(
                    boardService.searchBoardsForAdminWithStatus(categoryId, status, keyword, pageable)
            );
        } else if (categoryId != null) {
            return ResponseEntity.ok(
                    boardService.searchBoardsForAdmin(categoryId, keyword, pageable)
            );
        }
        return ResponseEntity.ok(boardService.getBoardsByStatus(status, pageable));
    }

    @Operation(summary = "관리자용 게시글 상태 변경", description = """
        관리자가 게시글의 상태를 변경합니다.
        변경 가능한 상태: PUBLISHED(공개), HIDDEN(숨김), DRAFT(임시저장)
        """)
    @ApiResponse(
            responseCode = "200",
            description = "게시글 상태 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\"boardIds\": [1, 2, 3, 4], \"status\": \"DRAFT\"}"
                    )
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/status")
    public ResponseEntity<Void> changePostsStatus(
            @RequestBody @Valid AdminBoardStatusRequest request
    ) {
        boardService.changeBoardStatus(request.getBoardIds(), request.getStatus());
        return ResponseEntity.noContent().build();
    }
}