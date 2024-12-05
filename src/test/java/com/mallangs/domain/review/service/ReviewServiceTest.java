package com.mallangs.domain.review.service;

import com.mallangs.domain.article.entity.MapVisibility;
import com.mallangs.domain.article.entity.PlaceArticle;
import com.mallangs.domain.article.repository.PlaceArticleRepository;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.util.GeometryUtil;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.review.dto.PageRequest;
import com.mallangs.domain.review.dto.ReviewCreateRequest;
import com.mallangs.domain.review.dto.ReviewInfoResponse;
import com.mallangs.domain.review.dto.ReviewUpdateRequest;
import com.mallangs.domain.review.entity.Review;
import com.mallangs.domain.review.entity.ReviewStatus;
import com.mallangs.domain.review.repository.ReviewRepository;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PlaceArticleRepository placeArticleRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AddressRepository addressRepository;
    private PasswordEncoder passwordEncoder;
    GeometryFactory geometryFactory = new GeometryFactory();

    @InjectMocks
    private ReviewService reviewService;

    private PlaceArticle placeArticle;
    private PlaceArticle placeArticle2;
    private Review review1;
    private Review review2;
    private Review review3;
    private Member member;
    private CustomMemberDetails customMemberDetails;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        Point seoul = geometryFactory.createPoint(new Coordinate(126.97, 37.57));
        Point ulsan = geometryFactory.createPoint(new Coordinate(129.31, 35.53));
        seoul.setSRID(4326);
        ulsan.setSRID(4326);

        member = Member.builder()
                .memberId(1L)
                .userId(new UserId("TestUser1"))
                .nickname(new Nickname("테스트"))
                .email(new Email("test@st.com"))
                .password(new Password("1Q2w3e4r!", passwordEncoder))
                .hasPet(false)
                .build();

        Address address = Address.builder()
                .member(member)
                .addressName("testAddress")
                .addressType("testAddressType")
                .mainAddressNo("testmainAddressNo")
                .point(GeometryUtil.createPoint(1, 2))
                .mountainYn("testMountainYn")
                .region1depthName("testRegion1depthName")
                .region2depthName("testRegion2depthName")
                .region3depthHName("testRegion3depthHName")
                .region3depthName("testRegion3depthName")
                .mainBuildingNo("testMainBuildingNo")
                .subBuildingNo("testSubBuildingNo")
                .roadName("testRoadName")
                .buildingName("testBuildingName")
                .subAddressNo("testSubAddressNo")
                .zoneNo("testZoneNo")
                .build();
        member.addAddress(address);

        placeArticle = PlaceArticle.builder()
                .id(1L)
                .title(" 서울")
                .address("서울주소")
                .geography(seoul)
                .mapVisibility(MapVisibility.VISIBLE)
                .build();

        placeArticle2 = PlaceArticle.builder()
                .id(2L)
                .title("울산")
                .address("울산주소")
                .geography(ulsan)
                .mapVisibility(MapVisibility.VISIBLE)
                .build();

        review1 = Review.builder()
                .reviewId(1L)
                .placeArticle(placeArticle)
                .member(member)
                .score(4)
                .content("좋아요!")
                .status(ReviewStatus.PUBLISHED)
                .build();

        review2 = Review.builder()
                .reviewId(2L)
                .placeArticle(placeArticle)
                .member(member)
                .score(5)
                .content("최고예요!")
                .status(ReviewStatus.PUBLISHED)
                .build();

        review3 = Review.builder()
                .reviewId(3L)
                .placeArticle(placeArticle2)
                .member(member)
                .score(2)
                .content("평범요!")
                .status(ReviewStatus.PUBLISHED)

                .build();

        customMemberDetails = new CustomMemberDetails(member);
    }


    @Test
    @DisplayName("리뷰 등록")
    void createReview() {
        //given
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .content("좋아요!")
                .score(4)
                .build();

        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(placeArticleRepository.findById(anyLong())).thenReturn(Optional.of(placeArticle));

        // when에서 reviewRepository.save의 동작을 정의하지 않고, any()로 설정
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review savedReview = invocation.getArgument(0); // save 메서드에 전달된 Review 객체 가져옴
            return savedReview;
        });

        //when
        ReviewInfoResponse response = reviewService.createReview(request, customMemberDetails, 1L);

        //then
        assertThat(response.getContent()).isEqualTo(request.getContent()); // request의 content와 response의 content 비교
        assertThat(response.getScore()).isEqualTo(request.getScore());   // request의 score와 response의 score 비교

        // verify를 통해 reviewRepository.save가 호출되었는지, 그리고 어떤 객체가 인자로 전달되었는지 확인
        verify(reviewRepository, times(1)).save(argThat(review ->
                review.getContent().equals(request.getContent()) && review.getScore() == request.getScore()
        ));
    }


    @Test
    @DisplayName("리뷰 수정")
    void updateReview() {
        // Given
        Long reviewId = 1L;
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .score(5)
                .content("수정된 내용")
                .image("newImage.jpg")
                .status(ReviewStatus.HIDDEN)
                .build();

        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(reviewRepository.findByReviewId(anyLong())).thenReturn(Optional.of(review1));
        // reviewRepository.save() 호출 시 변경된 review 객체 반환하도록 설정
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ReviewInfoResponse response = reviewService.updateReview(request, customMemberDetails, reviewId);

        // Then
        assertThat(response.getScore()).isEqualTo(request.getScore());
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.getImage()).isEqualTo(request.getImage());

        // Verify
        verify(reviewRepository).findByReviewId(reviewId);
        // save 메서드가 호출되었는지 확인
        verify(reviewRepository).save(any(Review.class));
    }
    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() {
        // Given
        Long reviewId = 1L;
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(reviewRepository.findByReviewId(anyLong())).thenReturn(Optional.of(review1));

        // When
        reviewService.deleteReview(customMemberDetails, reviewId);

        // Then
        verify(reviewRepository, times(1)).delete(review1);
    }


    @Test
    @DisplayName("리뷰 조회 (리뷰 ID로)")
    void getReviewById() {
        // Given
        Long reviewId = 1L;
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(reviewRepository.findByReviewId(anyLong())).thenReturn(Optional.of(review1));

        // When
        ReviewInfoResponse response = reviewService.getReviewById(customMemberDetails, reviewId);

        // Then
        assertThat(response.getReviewId()).isEqualTo(review1.getReviewId());
        assertThat(response.getScore()).isEqualTo(review1.getScore());
        assertThat(response.getContent()).isEqualTo(review1.getContent());

        // Verify
        verify(reviewRepository).findByReviewId(reviewId);
    }

    @Test
    @DisplayName("장소에 달린 리뷰 목록 조회")
    void getReviewsByPlaceArticleId() {
        // Given
        Long placeArticleId = 1L;
        PageRequest pageRequest = new PageRequest();
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        Page<Review> reviewPage = new PageImpl<>(reviews);


        // When
        when(reviewRepository.findByPlaceArticleId(anyLong(), any(Pageable.class))).thenReturn(reviewPage);
        Page<ReviewInfoResponse> responsePage = reviewService.getReviewsByPlaceArticleId(placeArticleId, pageRequest);

        // Then
        assertThat(responsePage.getContent()).hasSize(2);
        assertThat(responsePage.getContent().get(0).getReviewId()).isEqualTo(review1.getReviewId());
        assertThat(responsePage.getContent().get(1).getReviewId()).isEqualTo(review2.getReviewId());

    }

    @Test
    @DisplayName("장소에 달린 내 리뷰 조회")
    void getMyReviewByPlaceArticleId() {
        // Given
        Long placeArticleId = 1L;
        PageRequest pageRequest = new PageRequest();
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        Page<Review> reviewPage = new PageImpl<>(reviews);

        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(reviewRepository.findByPlaceArticleIdAndMemberId(anyLong(), anyLong(), any(Pageable.class))).thenReturn(reviewPage);

        // When
        Page<ReviewInfoResponse> response = reviewService.getMyReviewByPlaceArticleId(customMemberDetails, placeArticleId, pageRequest);

        // Then
        assertThat(response.getContent().get(0).getReviewId()).isEqualTo(review1.getReviewId());
    }

    @Test
    @DisplayName("내 리뷰 목록 조회")
    void getMyReviews() {
        // Given
        PageRequest pageRequest = new PageRequest();
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        Page<Review> reviewPage = new PageImpl<>(reviews);

        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(member));
        when(reviewRepository.findByMemberId(anyLong(), any(Pageable.class))).thenReturn(reviewPage);

        // When
        Page<ReviewInfoResponse> response = reviewService.getMyReviews(customMemberDetails, pageRequest);

        // Then
        assertThat(response.getContent()).hasSize(2);

    }

    @Test
    @DisplayName("특정 장소의 평균 평점 계산")
    void getAverageScoreByPlaceArticleId() {
        // Given
        Long placeArticleId = 1L;
        Double averageScore = 4.5;
        when(reviewRepository.getAverageScoreByPlaceArticleId(placeArticleId)).thenReturn(averageScore);

        // When
        Double result = reviewService.getAverageScoreByPlaceArticleId(placeArticleId);

        // Then
        assertThat(result).isEqualTo(averageScore);
    }
}
