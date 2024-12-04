package com.mallangs.domain.review.service;

import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.article.repository.PlaceArticleRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.PetResponse;
import com.mallangs.domain.review.dto.PageRequest;
import com.mallangs.domain.review.dto.ReviewCreateRequest;
import com.mallangs.domain.review.dto.ReviewInfoResponse;
import com.mallangs.domain.review.dto.ReviewUpdateRequest;
import com.mallangs.domain.review.entity.Review;
import com.mallangs.domain.review.entity.ReviewStatus;
import com.mallangs.domain.review.repository.ReviewRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceArticleRepository placeArticleRepository;
    private final MemberRepository memberRepository;

    //리뷰등록
    public ReviewInfoResponse createReview(ReviewCreateRequest reviewCreateRequest, CustomMemberDetails customMemberDetails, Long placeArticleId ) {
        Member member = getMember(customMemberDetails);

        PlaceArticle placeArticle = placeArticleRepository.findById(placeArticleId).orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

        try {
            Review review = reviewCreateRequest.toEntity(placeArticle, member);
            Review savedReview = reviewRepository.save(review);
            return new ReviewInfoResponse(savedReview);
        }catch (DataAccessException e) {
            log.error("Failed to create review: {}" ,e.getMessage());
            throw new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    //리뷰 수정
    public ReviewInfoResponse updateReview(ReviewUpdateRequest reviewUpdateRequest, CustomMemberDetails customMemberDetails, Long reviewId ) {
        Member member = getMember(customMemberDetails);
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));

        //본인꺼만 수정가능
        if (!review.getMember().equals(member)) {
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_OWNED);
        }
        try {
            review.change(
                    reviewUpdateRequest.getScore() != null ? reviewUpdateRequest.getScore() :review.getScore(),
                    reviewUpdateRequest.getContent() != null ? reviewUpdateRequest.getContent() :review.getContent(),
                    reviewUpdateRequest.getImage() != null ? reviewUpdateRequest.getImage() :review.getImage(),
                    reviewUpdateRequest.getStatus() != null ? reviewUpdateRequest.getStatus() :review.getStatus()
            );
            Review savedReview = reviewRepository.save(review);
            return new ReviewInfoResponse(savedReview);

        }catch (DataAccessException e) {
            log.error("Failed to update review: {}",e.getMessage());
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_UPDATED);
        }
    }

    //리뷰 삭제
    public void deleteReview(CustomMemberDetails customMemberDetails, Long reviewId) {
        Member member = getMember(customMemberDetails);
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 리뷰 작성자 확인
        if (!review.getMember().equals(member)) {
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_OWNED);
        }
        try {
            reviewRepository.delete(review);
        }catch (DataAccessException e) {
            log.error("Failed to delete review: {}" ,e.getMessage());
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_DELETE);
        }
    }

    //리뷰 조회 (리뷰 ID로)
    public ReviewInfoResponse getReviewById(CustomMemberDetails customMemberDetails, Long reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 비회원인 경우 공개된 정보만 반환
        if (customMemberDetails == null) {
            if (!review.getStatus().equals(ReviewStatus.HIDDEN)) {
                throw new MallangsCustomException(ErrorCode.REVIEW_NOT_OPEN);
            }
            return new ReviewInfoResponse(review); //
        }
        Member member = getMember(customMemberDetails);

        //본인꺼는 볼 수 있음
        if (!review.getMember().equals(member) && review.getStatus().equals(ReviewStatus.HIDDEN)) {
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_OPEN);

        }
        return new ReviewInfoResponse(review);
    }

    //장소에 달린 리뷰 목록 조회
    public Page<ReviewInfoResponse> getReviewsByPlaceArticleId(Long placeArticleId, PageRequest pageRequest) {
        try{
            Sort sort = Sort.by("reviewId").descending();
            Pageable pageable = pageRequest.getPageable(sort);
            Page<Review> reviews = reviewRepository.findByPlaceArticleId(placeArticleId, pageable);
            return reviews.map(ReviewInfoResponse::new);
        }catch (DataAccessException e) {
            log.error("Failed to get reviews by place article id: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
    }

    //장소에 달린 내 리뷰 조회
    public Page<ReviewInfoResponse> getMyReviewByPlaceArticleId(CustomMemberDetails customMemberDetails, Long placeArticleId, PageRequest pageRequest) {
        try {
            Sort sort = Sort.by("reviewId").descending();
            Pageable pageable = pageRequest.getPageable(sort);
            Member member = getMember(customMemberDetails);
            Page<Review> reviews = reviewRepository.findByPlaceArticleIdAndMemberId(placeArticleId, member.getMemberId(), pageable);
            return reviews.map(ReviewInfoResponse::new);
        }catch (DataAccessException e) {
            log.error("Failed to get reviews by place article Id: {}",e.getMessage());
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND);
        }

    }

    //내 리뷰 목록 조회
    public Page<ReviewInfoResponse> getMyReviews(CustomMemberDetails customMemberDetails, PageRequest pageRequest) {
        Member member = getMember(customMemberDetails);
        try {
            Sort sort = Sort.by("reviewId").descending();
            Pageable pageable = pageRequest.getPageable(sort);
            Page<Review> reviews = reviewRepository.findByMemberId(member.getMemberId(), pageable);
            return reviews.map(ReviewInfoResponse::new);
        }catch (DataAccessException e){
            log.error("Failed to get my reviews: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
    }

    //특정 장소의 평균 평점 계산
    public Double getAverageScoreByPlaceArticleId(Long placeArticleId) {
        return reviewRepository.getAverageScoreByPlaceArticleId(placeArticleId);
    }

    private Member getMember(CustomMemberDetails customMemberDetails) {
        UserId userId = new UserId(customMemberDetails.getUserId());

        return memberRepository.findByUserId(userId).orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
