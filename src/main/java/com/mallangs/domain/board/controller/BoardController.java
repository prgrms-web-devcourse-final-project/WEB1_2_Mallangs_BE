package com.mallangs.domain.board.controller;

import com.mallangs.domain.board.dto.request.CommunityCreateRequest;
import com.mallangs.domain.board.dto.request.CommunityUpdateRequest;
import com.mallangs.domain.board.dto.response.CommunityListResponse;
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

@Tag(name = "게시글 API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;

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
        Long postId = boardService.createCommunityBoard(request, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.created(URI.create("/api/board/community/" + postId)).body(postId);
    }

    @Operation(summary = "카테고리별 게시글 목록 조회", description = "특정 카테고리의 커뮤니티 게시글 목록을 조회합니다.")
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
}
