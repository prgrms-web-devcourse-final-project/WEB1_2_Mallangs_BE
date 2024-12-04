package com.mallangs.domain.comment.controller;

import com.mallangs.domain.comment.dto.request.CommentArticleRequest;
import com.mallangs.domain.comment.dto.request.CommentBoardRequest;
import com.mallangs.domain.comment.dto.request.CommentDeleteRequest;
import com.mallangs.domain.comment.dto.request.CommentPageRequest;
import com.mallangs.domain.comment.dto.request.CommentUpdateRequest;
import com.mallangs.domain.comment.dto.response.CommentResponse;
import com.mallangs.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "댓글", description = "댓글 CRUD")
public class CommentController {
    private final CommentService commentService;

    // 커뮤니티 댓글 등록
    @PostMapping("/board")
    @Operation(summary = "게시판 댓글 등록", description = "게시판 댓글을 등록하는 API")
    public ResponseEntity<CommentResponse> createBoardComment(@RequestBody CommentBoardRequest commentBoardRequest) {
        return ResponseEntity.ok(commentService.createBoardComment(commentBoardRequest));
    }

    // 글타래 댓글 등록
    @PostMapping("/article")
    @Operation(summary = "글타래 댓글 등록", description = "글타래 댓글을 등록하는 API")
    public ResponseEntity<CommentResponse> createArticleComment(@RequestBody CommentArticleRequest commentArticleRequest) {
        return ResponseEntity.ok(commentService.createArticleComment(commentArticleRequest));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정할 때 사용하는 API")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdateRequest));
    }

    // 댓글 삭제
    @DeleteMapping
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제할 때 사용하는 API")
    public ResponseEntity<Void> deleteComments(@RequestBody CommentDeleteRequest commentDeleteRequest) {
        commentService.deleteComments(commentDeleteRequest.getMemberId(), commentDeleteRequest.getCommentIds());
        return ResponseEntity.ok().build();
    }

    // Board 게시물에 대한 댓글 조회
    @GetMapping("/board/{boardId}")
    @Operation(summary = "커뮤니티 게시물 댓글 목록", description = "커뮤니티 게시물에 대한 댓글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<CommentResponse>> getBoardComments(@PathVariable Long boardId,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getPostComments(boardId, null, commentPageRequest));
    }

    // Article 게시물에 대한 댓글 조회
    @GetMapping("/article/{articleId}")
    @Operation(summary = "글타래 게시물 댓글 목록", description = "글타래 게시물에 대한 댓글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<CommentResponse>> getArticleComments(@PathVariable Long articleId,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getPostComments(null, articleId, commentPageRequest));
    }

    // 회원 댓글 목록 조회
    @GetMapping("/myComments/{memberId}")
    @Operation(summary = "내 댓글 목록 조회", description = "로그인한 사용자가 작성한 댓글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<CommentResponse>> getMyComments(@PathVariable Long memberId,
                                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getMyComments(memberId, commentPageRequest));
    }

    // 관리자 전체 댓글 목록 조회
    @GetMapping("/all")
    @Operation(summary = "전체 댓글 목록 조회", description = "관리자가 전체 댓글 목록을 조회할 때 사용하는 API")
    public ResponseEntity<Page<CommentResponse>> getAllComments(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getAllComments(commentPageRequest));
    }

    // 닉네임 별 댓글 목록 조회
    @GetMapping("/byNickname")
    @Operation(summary = "닉네임 별 댓글 목록 조회", description = "닉네임으로 댓글을 검색하는 API")
    public ResponseEntity<Page<CommentResponse>> getCommentsByNickname(@RequestParam String nickname,
                                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getCommentsByNickname(nickname, commentPageRequest));
    }

    // 댓글 내용 별 댓글 목록 조회
    @GetMapping("/byContent")
    @Operation(summary = "댓글 내용 별 댓글 목록 조회", description = "댓글 내용으로 댓글을 검색하는 API")
    public ResponseEntity<Page<CommentResponse>> getCommentsByContent(@RequestParam String content,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getCommentsByContent(content, commentPageRequest));
    }

    // 댓글 생성 범위 별 댓글 목록 조회
    @GetMapping("/dateRange")
    @Operation(summary = "날짜 범위 별 댓글 목록 조회", description = "특정 날짜 범위 내 댓글 목록을 조회하는 API")
    public ResponseEntity<Page<CommentResponse>> getCommentsByDateRange(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
                                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentPageRequest commentPageRequest = CommentPageRequest.builder().page(page).size(size).build();
        return ResponseEntity.ok(commentService.getCommentsByDateRange(startDate, endDate, commentPageRequest));
    }
}