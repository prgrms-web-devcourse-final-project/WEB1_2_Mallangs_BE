package com.mallangs.domain.review.repository;

import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 작성 리뷰 보기 (리뷰 ID로 조회)
    @Query("select r from Review r where r.reviewId = :reviewId")
    Optional<Review> findByReviewId(Long reviewId);

    // 장소에 달려있는 리뷰 목록 보기 (PlaceArticle ID로 조회)
    @Query("select r from Review r where r.placeArticle.id = :placeArticleId")
    Page<Review> findByPlaceArticleId(Long placeArticleId, Pageable pageable);

    // 장소에 달려있는 리뷰 중에 내 리뷰 보기 (PlaceArticle ID와 Member ID로 조회)
    @Query("select r from Review r where r.placeArticle.id = :placeArticleId and r.member.memberId = :memberId")
    Page<Review> findByPlaceArticleIdAndMemberId(@Param("placeArticleId") Long placeArticleId, @Param("memberId") Long memberId, Pageable pageable);

    // 내 리뷰 목록 보기 (Member ID로 조회)
    @Query("select r from Review r where r.member.memberId = :memberId")
    Page<Review> findByMemberId(Long memberId, Pageable pageable);

    // 특정 장소의 평균 평점 계산
    @Query("select avg(r.score) from Review r where r.placeArticle.id = :placeArticleId")
    Double getAverageScoreByPlaceArticleId(Long placeArticleId);

}