package com.mallangs.domain.review.repository;

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
import com.mallangs.domain.review.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PlaceArticleRepository placeArticleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;
    GeometryFactory geometryFactory = new GeometryFactory();

    private PlaceArticle placeArticle;
    private PlaceArticle placeArticle2;
    private Review review1;
    private Review review2;
    private Review review3;
    private Member member;

    @BeforeEach
    void setUp(){
        passwordEncoder = new BCryptPasswordEncoder();
        Point seoul = geometryFactory.createPoint(new Coordinate(126.97, 37.57)); // 경도, 위도 순서
        Point ulsan = geometryFactory.createPoint(new Coordinate(129.31, 35.53)); // 경도, 위도 순서
        seoul.setSRID(4326);
        ulsan.setSRID(4326);
        member = Member.builder()
                .userId(new UserId("TestUser1"))
                .nickname(new Nickname("테스트"))
                .email(new Email("test@st.com"))
                .password(new Password("1Q2w3e4r!", passwordEncoder))
                .hasPet(false)
                .build();
        memberRepository.save(member);

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
        addressRepository.save(address);
        member.addAddress(address);
        memberRepository.save(member);

        //PlaceArticle 생성
        placeArticle = PlaceArticle.builder()
                .title(" 서울")
                .address("서울주소")
                .geography(seoul)
                .mapVisibility(MapVisibility.VISIBLE)
                .build();
        placeArticleRepository.save(placeArticle);

        //PlaceArticle 생성
        placeArticle2 = PlaceArticle.builder()
                .title("울산")
                .address("울산주소")
                .geography(ulsan)
                .mapVisibility(MapVisibility.VISIBLE)
                .build();
        placeArticleRepository.save(placeArticle2);

        // Review 생성 및 저장
        review1 = Review.builder()
                .placeArticle(placeArticle)
                .member(member)
                .score(4)
                .content("좋아요!")
                .build();
        reviewRepository.save(review1);

        review2 = Review.builder()
                .placeArticle(placeArticle)
                .member(member)
                .score(5)
                .content("최고예요!")
                .build();
        reviewRepository.save(review2);

        review3 = Review.builder()
                .placeArticle(placeArticle2)
                .member(member)
                .score(5)
                .content("최고예요!")
                .build();
        reviewRepository.save(review3);
    }

    @Test
    @DisplayName("리뷰 ID로 리뷰 조회")
    void findByReviewId() {
        //when
        Optional<Review> findReview = reviewRepository.findByReviewId(review1.getReviewId());

        // then
        assertThat(findReview).isPresent();
        assertThat(findReview.get().getReviewId()).isEqualTo(review1.getReviewId());
        assertThat(findReview.get().getContent()).isEqualTo(review1.getContent());
    }

    @Test
    @DisplayName("장소 ID로 리뷰 목록 조회")
    void findByPlaceArticleId() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewId").descending());

        // when
        Page<Review> reviewPage = reviewRepository.findByPlaceArticleId(placeArticle.getId(), pageable);
        Page<Review> reviewPage2 = reviewRepository.findByPlaceArticleId(placeArticle2.getId(), pageable);

        // then
        assertThat(reviewPage).isNotEmpty();
        assertThat(reviewPage.getTotalElements()).isEqualTo(2);
        assertThat(reviewPage2.getTotalElements()).isEqualTo(1);
        assertThat(reviewPage.getContent().get(0).getReviewId()).isEqualTo(review2.getReviewId());
        assertThat(reviewPage2.getContent().get(0).getReviewId()).isEqualTo(review3.getReviewId());
    }


    @Test
    @DisplayName("장소 ID와 멤버 ID로 리뷰 조회")
    void findByPlaceArticleIdAndMemberId() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewId").descending());
        // when
        Page<Review> findReview = reviewRepository.findByPlaceArticleIdAndMemberId(placeArticle.getId(), member.getMemberId(),pageable);

        // then
        assertThat(findReview).isNotEmpty();
        assertThat(findReview.getContent().get(0).getReviewId()).isEqualTo(review2.getReviewId());
        assertThat(findReview.getContent().get(0).getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    @DisplayName("멤버 ID로 리뷰목록 조회")
    void findByMemberId() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<Review> reviewPage = reviewRepository.findByMemberId(member.getMemberId(), pageable);
        //then
        assertThat(reviewPage).isNotEmpty();
        assertThat(reviewPage.getTotalElements()).isEqualTo(3);
        assertThat(reviewPage.getContent().get(0).getReviewId()).isEqualTo(review1.getReviewId());
    }

    @Test
    @DisplayName("장소 ID로 평균 점수 계산")
    void getAverageScoreByPlaceArticleId() {
        // when
        Double averageScore = reviewRepository.getAverageScoreByPlaceArticleId(placeArticle.getId());
        Double averageScore2 = reviewRepository.getAverageScoreByPlaceArticleId(placeArticle2.getId());

        // then
        assertThat(averageScore).isEqualTo(4.5);
        assertThat(averageScore2).isEqualTo(5);
    }
}