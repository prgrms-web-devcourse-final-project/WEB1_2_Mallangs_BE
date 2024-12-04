package com.mallangs.domain.review.controller;


import com.mallangs.domain.review.dto.PageRequest;
import com.mallangs.domain.review.dto.ReviewCreateRequest;
import com.mallangs.domain.review.dto.ReviewInfoResponse;
import com.mallangs.domain.review.dto.ReviewUpdateRequest;
import com.mallangs.domain.review.service.ReviewService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/place-articles")
@Tag(name = "리뷰", description = "장소 리뷰 API (CRUD). 장소 글타래는 일반 글타래와 동일하게 articleId를 고유 식별자로 사용하지만, 명확성을 위해 placeArticleId를 엔드포인트에 사용합니다. (받는 값은 articleId)")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{placeArticleId}/reviews")
    @Operation(summary = "리뷰 등록", description = "리뷰를 등록할때 사용하는 API")
    public ResponseEntity<ReviewInfoResponse> createReview(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                           @PathVariable Long placeArticleId,
                                                           @Valid @RequestBody ReviewCreateRequest reviewCreateRequest) {
        ReviewInfoResponse response = reviewService.createReview(reviewCreateRequest, customMemberDetails, placeArticleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 리뷰 수정
    // [참고] placeArticleId는 API 경로 통일성을 위해 받지만, 실제로는 reviewId로만 식별하므로 사용하지 않음, 향후 변경가능성 고려
    @PutMapping("/{placeArticleId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정할때 사용하는 API")
    public ResponseEntity<ReviewInfoResponse> updateReview(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                           @PathVariable Long placeArticleId,
                                                           @PathVariable Long reviewId,
                                                           @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
        ReviewInfoResponse response = reviewService.updateReview(reviewUpdateRequest, customMemberDetails, reviewId);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/{placeArticleId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제하는 API (DB에서 완전삭제), 안보이게만 하고 싶은 경우 리뷰 수정 -> 숨김")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                             @PathVariable Long placeArticleId,
                                             @PathVariable Long reviewId) {
        reviewService.deleteReview(customMemberDetails, reviewId);
        return ResponseEntity.noContent().build();
    }

    // 리뷰 조회 (리뷰 ID로)
    @GetMapping("/{placeArticleId}/reviews/{reviewId}")
    @Operation(summary = "리뷰 조회", description = "리뷰 ID로 리뷰를 조회하는 API.")
    public ResponseEntity<ReviewInfoResponse> getReviewById(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                            @PathVariable Long placeArticleId,
                                                            @PathVariable Long reviewId) {
        ReviewInfoResponse response = reviewService.getReviewById(customMemberDetails, reviewId);
        return ResponseEntity.ok(response);
    }

    // 장소에 달린 리뷰 목록 조회
    @GetMapping("/{placeArticleId}/reviews")
    @Operation(summary = "장소 리뷰 목록 조회", description = "특정 장소에 달린 리뷰 목록을 조회하는 API.")
    public ResponseEntity<Page<ReviewInfoResponse>> getReviewsByPlaceArticleId(@PathVariable Long placeArticleId,
                                                                               PageRequest pageRequest) {
        Page<ReviewInfoResponse> response = reviewService.getReviewsByPlaceArticleId(placeArticleId, pageRequest);
        return ResponseEntity.ok(response);
    }

    // 장소에 달린 내 리뷰 조회
    @GetMapping("/{placeArticleId}/reviews/my")
    @Operation(summary = "특정장소의 내 리뷰 목록 조회", description = "특정 장소에 달린 내 리뷰 목록을 조회하는 API.")
    public ResponseEntity<Page<ReviewInfoResponse>> getMyReviewByPlaceArticleId(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                                                @PathVariable Long placeArticleId,
                                                                                PageRequest pageRequest) {
        Page<ReviewInfoResponse> response = reviewService.getMyReviewByPlaceArticleId(customMemberDetails, placeArticleId, pageRequest);
        return ResponseEntity.ok(response);
    }

    // 내 리뷰 목록 조회
    @GetMapping(path = "/my-reviews")
    @Operation(summary = "내 전체 리뷰 목록 조회", description = "내가 작성한 모든 리뷰 목록을 조회하는 API.")
    public ResponseEntity<Page<ReviewInfoResponse>> getMyReviews(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                                 PageRequest pageRequest) {
        Page<ReviewInfoResponse> response = reviewService.getMyReviews(customMemberDetails, pageRequest);
        return ResponseEntity.ok(response);
    }

    // 특정 장소의 평균 평점 계산
    @GetMapping("/{placeArticleId}/reviews/average-score")
    @Operation(summary = "평균 평점 조회", description = "특정 장소의 평균 평점을 조회하는 API, 게시상태가 PUBLISH(공개) 상태인 것만.")
    public ResponseEntity<Double> getAverageScoreByPlaceArticleId(@PathVariable Long placeArticleId) {
        Double averageScore = reviewService.getAverageScoreByPlaceArticleId(placeArticleId);
        return ResponseEntity.ok(averageScore);
    }
}