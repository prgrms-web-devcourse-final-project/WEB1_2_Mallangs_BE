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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

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

    @Operation(summary = "커뮤니티 전체 게시글 조회", description = "커뮤니티 게시판의 게시글 전체를 조회합니다.")
    @GetMapping("/community/all")
    public ResponseEntity<Page<CommunityListResponse>> getAllCommunity(@RequestParam(defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getAllCommunitiyBoard(pageRequest));
    }

    @Operation(summary = "커뮤니티 게시글 검색", description = "키워드로 커뮤니티 게시글을 검색합니다.")
    @GetMapping("/community/search")
    public ResponseEntity<Page<CommunityListResponse>> searchCommunityPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page
    ) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.searchCommunityBoards(keyword, pageRequest));
    }

    @Operation(summary = "커뮤니티 게시글 회원으로 조회", description = "특정 회원이 작성한 커뮤니티 게시글 목록을 조회합니다.")
    @GetMapping("/community/member/{memberId}")
    public ResponseEntity<Page<CommunityListResponse>> getMemberCommunityPosts(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getMemberCommunityBoards(memberId, pageRequest));
    }

    @Operation(summary = "커뮤니티 카테고리별 게시글 목록 조회", description = "특정 카테고리의 커뮤니티 게시글 목록을 조회합니다.")
    @GetMapping("/community/category/{categoryId}")
    public ResponseEntity<Page<CommunityListResponse>> getCommunityPostByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getCommunityBoardsByCategory(categoryId, pageRequest));
    }

    @Operation(summary = "커뮤니티 게시글 상세 조회", description = "특정 커뮤니티 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/community/{boardId}")
    public ResponseEntity<CommunityDetailResponse> getCommunityPost(
            @Parameter(description = "게시글 ID") @PathVariable Long boardId
    ) {
        return ResponseEntity.ok(boardService.getCommunityBoard(boardId));
    }

    @Operation(summary = "커뮤니티 게시글 수정", description = "기존 커뮤니티 게시글을 수정합니다.")
    @PutMapping("/community/{boardId}")
    public ResponseEntity<Void> updateCommunityPost(
            @PathVariable Long boardId,
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

    @Operation(summary = "실종신고-목격제보 전체 게시글 조회", description = "실종신고-목격제보 게시판의 게시글 전체를 조회합니다.")
    @GetMapping("/sighting/all")
    public ResponseEntity<Page<SightingListResponse>> getAllSighting(@RequestParam(defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getAllSightingBoard(pageRequest));
    }

    @Operation(summary = "실종신고-목격제보 게시글 검색", description = "키워드로 실종신고-목격제보 게시글을 검색합니다.")
    @GetMapping("/sighting/search")
    public ResponseEntity<Page<SightingListResponse>> searchSightingPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.searchSightingBoards(keyword, pageRequest));
    }

    @Operation(summary = "실종신고-목격제보 게시글 회원으로 조회", description = "특정 회원이 작성한 실종신고-목격제보 게시글 목록을 조회합니다.")
    @GetMapping("/sighting/member/{memberId}")
    public ResponseEntity<Page<SightingListResponse>> getMemberSightingPosts(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getMemberSightingBoards(memberId, pageRequest));
    }

    @Operation(summary = "실종신고-목격제보 카테고리별 목격 게시글 목록 조회", description = "특정 카테고리의 실종신고-목격제보 게시글 목록을 조회합니다.")
    @GetMapping("/sighting/category/{categoryId}")
    public ResponseEntity<Page<SightingListResponse>> getSightingPostsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return ResponseEntity.ok(boardService.getSightingBoardsByCategory(categoryId, pageRequest));
    }

    @Operation(summary = "실종신고-목격제보 게시글 상세 조회", description = "특정 실종신고-목격제보 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/sighting/{boardId}")
    public ResponseEntity<SightingDetailResponse> getSightingPost(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getSightingBoard(boardId));
    }

    @Operation(summary = "실종신고-목격제보 게시글 수정", description = "기존 실종신고-목격제보 게시글을 수정합니다.")
    @PutMapping("/sighting/{boardId}")
    public ResponseEntity<Void> updateSightingPost(
            @PathVariable Long boardId,
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
            @PathVariable Long boardId,
            @RequestParam BoardType boardType,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        boardService.deleteBoard(boardId, customMemberDetails.getMemberId(), boardType);
        return ResponseEntity.noContent().build();
    }

    // 관리자 기능
    @Operation(summary = "게시글 검색 - 관리자 권한", description = "관리자가 게시글을 검색하고 필터링합니다.")
    @GetMapping("/admin/search")
    public ResponseEntity<AdminBoardsResponse> searchPosts(
            @RequestParam(required = false) BoardType boardType,
            @RequestParam(required = false) BoardStatus boardStatus,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        if (boardStatus != null && categoryId != null) {
            return ResponseEntity.ok(boardService.searchBoardsForAdminWithStatus(categoryId, boardType, boardStatus, keyword, pageRequest));
        } else if (categoryId != null) {
            return ResponseEntity.ok(boardService.searchBoardsForAdmin(categoryId, boardType, keyword, pageRequest));
        }
        return ResponseEntity.ok(boardService.getBoardsByStatus(boardStatus, pageRequest));
    }

    @Operation(summary = "게시글 상태 변경 - 관리자 권한", description = "관리자가 게시글의 상태를 변경합니다. 다중 선택 가능")
    @PatchMapping("/admin/status")
    public ResponseEntity<Void> changePostsStatus(@RequestBody @Valid AdminBoardStatusRequest request) {
        boardService.changeBoardStatus(request.getBoardIds(), request.getStatus());
        return ResponseEntity.noContent().build();
    }
}
