package com.mallangs.domain.board.controller;

import com.mallangs.domain.board.dto.request.*;
import com.mallangs.domain.board.dto.response.*;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.board.service.BoardService;
import com.mallangs.domain.board.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
    })
    @PostMapping("/community")
    public ResponseEntity<Long> createCommunity(@Valid @RequestBody CommunityCreateRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        Long boardId = boardService.createCommunityBoard(request, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.created(URI.create("/api/board/community/" + boardId)).body(boardId);
    }

    @Operation(summary = "커뮤니티 카테고리별 게시글 목록 조회", description = "특정 카테고리의 커뮤니티 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않습니다.")
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @PutMapping("/community/{boardId}")
    public ResponseEntity<Void> updateCommunityPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Valid @RequestBody CommunityUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boardService.updateCommunityBoard(boardId, request, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }

    // 실종신고-목격제보 관련
    @Operation(summary = "실종신고-목격제보 게시글 작성", description = "새로운 실종신고-목격제보 게시글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/sighting")
    public ResponseEntity<Long> createSightingPost(
            @Valid @RequestBody SightingCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long boardId = boardService.createSightingBoard(request, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.created(URI.create("/api/board/sighting/" + boardId)).body(boardId);
    }

    @Operation(summary = "실종신고-목격제보 카테고리별 목격 게시글 목록 조회", description = "특정 카테고리의 실종신고-목격제보 게시글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음")
    })
    @GetMapping("/sighting/category/{categoryId}")
    public ResponseEntity<Page<SightingListResponse>> getSightingPostsByCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
            @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(boardService.getSightingBoardsByCategory(categoryId, pageable));
    }

    @Operation(summary = "실종신고-목격제보 게시글 상세 조회", description = "특정 실종신고-목격제보 게시글의 상세 내용을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @GetMapping("/sighting/{boardId}")
    public ResponseEntity<SightingDetailResponse> getSightingPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId
    ) {
        return ResponseEntity.ok(boardService.getSightingBoard(boardId));
    }

    @Operation(summary = "실종신고-목격제보 게시글 수정", description = "기존 실종신고-목격제보 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @PutMapping("/sighting/{boardId}")
    public ResponseEntity<Void> updateSightingPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Valid @RequestBody SightingUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boardService.updateSightingBoard(boardId, request, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }

    // 공통 기능
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
    })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId,
            @Parameter(description = "게시글 타입") @RequestParam BoardType boardType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boardService.deleteBoard(boardId, Long.parseLong(userDetails.getUsername()), boardType);
        return ResponseEntity.noContent().build();
    }

    // 관리자 기능
    @Operation(summary = "관리자용 게시글 검색", description = "관리자가 게시글을 검색하고 필터링합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @GetMapping("/admin/search")
    public ResponseEntity<Page<AdminBoardResponse>> searchPosts(
            @Parameter(description = "게시글 상태") @RequestParam(required = false) BoardStatus status,
            @Parameter(description = "게시글 타입") @RequestParam(required = false) BoardType boardType,
            @Parameter(description = "카테고리 ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지네이션 정보") @PageableDefault(size = 10) Pageable pageable
    ) {
        if (status != null && categoryId != null) {
            return ResponseEntity.ok(
                    boardService.searchBoardsForAdminWithStatus(categoryId, status, keyword, boardType, pageable)
            );
        } else if (categoryId != null) {
            return ResponseEntity.ok(
                    boardService.searchBoardsForAdmin(categoryId, keyword, boardType, pageable)
            );
        }
        return ResponseEntity.ok(boardService.getBoardsByStatus(status, boardType, pageable));
    }

    @Operation(summary = "관리자용 게시글 상태 변경", description = "관리자가 게시글의 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PatchMapping("/admin/status")
    public ResponseEntity<Void> changePostsStatus(
            @RequestBody @Valid AdminBoardStatusRequest request
    ) {
        boardService.changeBoardStatus(request.getBoardIds(), request.getStatus());
        return ResponseEntity.noContent().build();
    }
}
