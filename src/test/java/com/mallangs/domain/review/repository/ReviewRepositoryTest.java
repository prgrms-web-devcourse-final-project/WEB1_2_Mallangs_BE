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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    private PlaceArticle placeArticle;
    private PlaceArticle placeArticle2;
    private Review review1;
    private Review review2;
    private Member member;

    @BeforeEach
    void setUp(){
        passwordEncoder = new BCryptPasswordEncoder();

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
                .latitude(37.57)
                .longitude(126.97)
                .mapVisibility(MapVisibility.VISIBLE)
                .build();
        placeArticleRepository.save(placeArticle);

        //PlaceArticle 생성
        placeArticle2 = PlaceArticle.builder()
                .title("울산")
                .address("울산주소")
                .latitude(35.53)
                .longitude(129.31)
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
    void findByPlaceArticleId() {
    }

    @Test
    void findByPlaceArticleIdAndMemberId() {
    }

    @Test
    void findByMemberId() {
    }

    @Test
    void getAverageScoreByPlaceArticleId() {
    }
}