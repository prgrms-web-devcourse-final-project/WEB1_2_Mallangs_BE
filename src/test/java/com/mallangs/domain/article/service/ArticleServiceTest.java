package com.mallangs.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mallangs.domain.article.dto.request.LostCreateRequest;
import com.mallangs.domain.article.dto.request.PlaceCreateRequest;
import com.mallangs.domain.article.dto.request.RescueCreateRequest;
import com.mallangs.domain.article.dto.response.ArticleResponse;
import com.mallangs.domain.article.dto.response.LostResponse;
import com.mallangs.domain.article.dto.response.PlaceResponse;
import com.mallangs.domain.article.entity.LostStatus;
import com.mallangs.domain.article.entity.MapVisibility;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ArticleServiceTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ArticleService articleService;

  private Member testMember;
//  private LostArticle testLost;
//  private PlaceArticle testPlace;
//  private RescueArticle testRescue;

  @BeforeEach
  void setUp() {
    testMember = createTestMember();
    memberRepository.save(testMember);

//    testLost = createLostArticle();
//    testPlace = createPlaceArticle();
//    testRescue = createRescueArticle();

  }

  @Test
  @DisplayName("유효한 데이터로 Article 을 생성할 수 있어야 한다.")
  void shouldCreateLostArticle() {
    //given
    LostCreateRequest lostCreateRequest = createTestLostRequest(testMember);

    //when
    ArticleResponse articleResponse = articleService.createArticle(lostCreateRequest,
        testMember.getMemberId());

    //then
    assertThat(articleResponse).isInstanceOf(LostResponse.class);
    LostResponse lostResponse = (LostResponse) articleResponse;

    assertThat(lostResponse.getTitle()).isEqualTo("우리집 강아지 찾아요!");
    assertThat(lostResponse.getDescription()).isEqualTo("강아지를 찾아주세요.");
    assertThat(lostResponse.getMapVisibility()).isEqualTo(MapVisibility.VISIBLE);
    assertThat(lostResponse.getLostDate()).isEqualTo(LocalDate.of(2024, 11, 10));
    assertThat(lostResponse.getChipNumber()).isEqualTo("12321-67890");
  }

  // 등록 시 필드값 없으면 예외 발생
  @Test
  @DisplayName("필수 필드가 누락된 경우 IllegalArgumentException 이 발생해야 한다.")
  void shouldThrowExceptionWhenMandatoryFieldsAreMissing() {
    //given

    //when

    //then

  }

  //
  // 조회 - 바른 Id 조회
  @Test
  @DisplayName("유효한 ID로 Article 을 조회할 수 있어야 한다.")
  void shouldRetrieveLostArticleById() {
    //given
    LostCreateRequest lostCreateRequest = createTestLostRequest(testMember);

    ArticleResponse articleResponse = articleService.createArticle(lostCreateRequest,
        testMember.getMemberId());
    assertThat(articleResponse).isInstanceOf(LostResponse.class);

    //when
    ArticleResponse foundArticleResponse = articleService.getArticleById(
        articleResponse.getArticleId());

    //then
    assertThat(foundArticleResponse).isInstanceOf(LostResponse.class);
    // 서버 측에서는 ArticleResponse 형태로 반환
    // 클라이언트 측에서 형변환
    LostResponse lostResponse = (LostResponse) articleResponse;

    assertThat(lostResponse.getTitle()).isEqualTo("우리집 강아지 찾아요!");
    assertThat(lostResponse.getDescription()).isEqualTo("강아지를 찾아주세요.");
    assertThat(lostResponse.getMapVisibility()).isEqualTo(MapVisibility.VISIBLE);
    assertThat(lostResponse.getLostDate()).isEqualTo(LocalDate.of(2024, 11, 10));
    assertThat(lostResponse.getChipNumber()).isEqualTo("12321-67890");

  }

  // 조회 - 페이징 조회
  @Test
  @DisplayName("Article 목록을 페이징 처리하여 조회할 수 있어야 한다.")
  void shouldRetrieveArticlesWithPagination() {
    //given
    LostCreateRequest lostCreateRequest = createTestLostRequest(testMember);
    PlaceCreateRequest placeCreateRequest = createTestPlaceRequest(testMember);
    RescueCreateRequest rescueCreateRequest = createTestRescueRequest(testMember);

    articleService.createArticle(lostCreateRequest, testMember.getMemberId());
    articleService.createArticle(placeCreateRequest, testMember.getMemberId());
    articleService.createArticle(rescueCreateRequest, testMember.getMemberId());

    //when
    Pageable pageable = PageRequest.of(0, 2);
    Page<ArticleResponse> articleResponsePage = articleService.findAllTypeArticles(pageable);

    //then
    assertThat(articleResponsePage).isNotNull();
    assertThat(articleResponsePage.getTotalElements()).isEqualTo(3);
    assertThat(articleResponsePage.getContent()).hasSize(2);

    ArticleResponse firstArticleResponse = articleResponsePage.getContent().get(0);
    assertThat(firstArticleResponse).isInstanceOf(ArticleResponse.class);
    ArticleResponse secondArticleResponse = articleResponsePage.getContent().get(1);
    assertThat(secondArticleResponse).isInstanceOf(PlaceResponse.class);

    assertThat(firstArticleResponse.getTitle()).isEqualTo("우리집 강아지 찾아요!");
    // 클라이언트 형변환
    PlaceResponse secondArticleResponseTypeChanged = (PlaceResponse) secondArticleResponse;
    assertThat(secondArticleResponseTypeChanged.getBusinessHours()).isEqualTo("09:00~20:00");

  }

  // 특정 조건 조회 (상태, 카테고리 등) - 하나의 카테고리 목록 조회
  @Test
  @DisplayName("특정 조건(상태, 카테고리 등)으로 Article 을 조회할 수 있어야 한다.")
  void shouldRetrieveArticlesByCondition() {
    //given

    //when

    //then

  }

  // 여러 종류의 글 조회
  @Test
  @DisplayName("여러 조건(상태, 카테고리 등)으로 Article 을 조회할 수 있어야 한다.")
  void shouldRetrieveArticlesByMultipleConditions() {
    //given

    //when

    //then

  }

  // 없는 값 조회 시 예외 발생
  @Test
  @DisplayName("존재하지 않는 ID로 조회할 경우 예외를 발생시켜야 한다.")
  void shouldThrowExceptionWhenArticleNotFoundById() {
    //given

    //when

    //then

  }

  // 정렬 조회
  @Test
  @DisplayName("Article 을 지정된 정렬 조건으로 조회할 수 있어야 한다.")
  void shouldRetrieveArticlesWithSorting() {
    //given

    //when

    //then

  }

  //
  // 수정 - 유효한 값으로 업데이트
  @Test
  @DisplayName("유효한 값으로 LostArticle 을 업데이트할 수 있어야 한다.")
  void shouldUpdateLostArticleWithValidData() {
    //given

    //when

    //then

  }

  // 상태 전환 테스트 - 부분 필드
  @Test
  @DisplayName("LostArticle 의 상태를 전환할 수 있어야 한다.")
  void shouldChangeLostArticleStatus() {
    //given

    //when

    //then

  }

  // 불변 필드 수정 시 예외
  @Test
  @DisplayName("불변 필드 수정 시 IllegalArgumentException 이 발생해야 한다.")
  void shouldThrowExceptionWhenImmutableFieldIsUpdated() {
    //given

    //when

    //then

  }

  // 연관 객체 업데이트 테스트
  // - 작성한 글목록? 같은 것 있으면 체크
  @Test
  @DisplayName("LostArticle 의 연관 필드를 업데이트할 수 있어야 한다.")
  void shouldUpdateRelatedFieldsOfLostArticle() {
    //given

    //when

    //then

  }

  // 유효하지 않은 데이터 예외
  @Test
  @DisplayName("유효하지 않은 데이터로 업데이트 시 예외를 발생시켜야 한다.")
  void shouldThrowExceptionWhenUpdatingWithInvalidData() {
    //given

    //when

    //then

  }

  // 권한 - 권한 없는 사용자가 article 수정

  // 동시성 - 여러 사용자가 동시에 수정 삭제


  //
  // 삭제 - 유효한 데이터 삭제 - 물리 삭제
  @Test
  @DisplayName("유효한 데이터로 LostArticle 을 삭제할 수 있어야 한다.")
  void shouldDeleteLostArticleWithValidData() {
    //given

    //when

    //then

  }

  // 논리 삭제 - 데이터 숨김처리?
  @Test
  @DisplayName("LostArticle 을 논리적으로 삭제할 수 있어야 한다.")
  void shouldLogicallyDeleteLostArticle() {
    //given

    //when

    //then

  }


  // 없는 데이터 삭제
  @Test
  @DisplayName("존재하지 않는 데이터 삭제 시 예외를 발생시켜야 한다.")
  void shouldThrowExceptionWhenDeletingNonExistentArticle() {
    //given

    //when

    //then

  }


  // 연관 데이터 삭제
  @Test
  @DisplayName("LostArticle 삭제 시 연관 데이터가 함께 삭제되어야 한다.")
  void shouldCascadeDeleteRelatedDataWhenArticleIsDeleted() {
    //given

    //when

    //then

  }

  private Member createTestMember() {
    return Member.builder()
        .userId(new UserId("testId1234"))
        .password(new Password("122234Aa1!!", passwordEncoder))
        .email(new Email("test123@test.com"))
        .nickname(new Nickname("testNickName"))
        .hasPet(true)
        .build();
  }

  private LostCreateRequest createTestLostRequest(Member testMember) {
    return LostCreateRequest.builder()
        .type("lost")
        .articleStatus(BoardStatus.PUBLISHED)
        .title("우리집 강아지 찾아요!")
        .longitude(-73.968285)
        .latitude(40.785091)
        .description("강아지를 찾아주세요.")
        .contact("010-1234-5678")
        .image("lost_pet_image.jpg")
        .petType(PetType.DOG)
        .breed("말티즈")
        .name("멍멍")
        .petAge(3)
        .petGender(PetGender.MALE)
        .isNeutering(true)
        .chipNumber("12321-67890")
        .lostDate(LocalDate.of(2024, 11, 10))
        .lastSeenLocation("편의점")
        .lostStatus(LostStatus.LOST)
        .build();
  }

  private RescueCreateRequest createTestRescueRequest(Member testMember) {
    return RescueCreateRequest.builder()
        .type("rescue")
        .articleStatus(BoardStatus.PUBLISHED)
        .title("구조가 필요합니다.")
        .longitude(-33.968285)
        .latitude(40.785091)
        .description("고양이를 구해주세요.")
        .image("rescue_pet_image.jpg")
        .petType(PetType.CAT)
        .build();

  }

  private PlaceCreateRequest createTestPlaceRequest(Member testMember) {
    return PlaceCreateRequest.builder()
        .type("place")
        .articleStatus(BoardStatus.PUBLISHED)
        .title("사랑 동물 병원")
        .longitude(-75.968285)
        .latitude(40.785091)
        .description("동물 병원입니다.")
        .contact("010-8888-0000")
        .image("place_pet_image.jpg")
        .businessHours("09:00~20:00")
        .closeDays("월,수")
        .website("http://www.saranganimalhospital.com")
        .category("testCategory")
        .address("testAddress")
        .roadAddress("testRoadAddress")
        .hasParking(true)
        .isPetFriendly(true)
        .build();
  }

//  private LostArticle createLostArticle(Member testMember) {
//
//  }
//
//  private PlaceArticle createLostArticle(Member testMember) {
//
//  }
//
//  private RescueArticle createLostArticle(Member testMember) {
//
//  }


}