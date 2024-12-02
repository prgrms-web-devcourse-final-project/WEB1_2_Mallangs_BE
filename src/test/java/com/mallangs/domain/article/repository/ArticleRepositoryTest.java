package com.mallangs.domain.article.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.entity.LostArticle;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticleRepositoryTest { // db 와의 crud 체크

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Member testMember;
  private final GeometryFactory geometryFactory = new GeometryFactory();

  @BeforeEach
  void setUp() {
    testMember = createTestMember();
    memberRepository.save(testMember);
  }

  @Test
  @DisplayName("LostArticle 을 등록 및 조회할 수 있어야 한다.")
  void saveAndFindLostArticle() {
    //given
    LostArticle lostArticle = LostArticle.builder()
        .breed("강아지종")
        .petType(PetType.DOG)
        .name("버디")
        .petAge(3)
        .petGender(PetGender.MALE)
        .isNeutering(true)
        .chipNumber("12345-67890")
        .lostDate(LocalDate.of(2024, 11, 20))
        .lastSeenLocation("공원")
        .lostStatus(LostStatus.LOST)
        .mapVisibility(MapVisibility.VISIBLE)
            .articleStatus(BoardStatus.DRAFT)
        .type("lost")
        .title("강아지 찾아주세요")
        .geography(changeToPoint(-73.968285, 40.785091))
        .description("설명")
        .contact("번호")
        .image("이미지")
        .member(testMember)
        .build();

    //when
    Article savedLostArticle = articleRepository.save(lostArticle);

    //then
    assertThat(savedLostArticle.getId()).isNotNull();
    assertThat(lostArticle.getTitle()).isEqualTo(savedLostArticle.getTitle());
    assertThat(lostArticle.getDescription()).isEqualTo(savedLostArticle.getDescription());

  }

  // 수정
  @Test
  @DisplayName("LostArticle 을 수정할 수 있어야 한다.")
  void changeLostArticle() {
    //given
    LostArticle lostArticle = LostArticle.builder()
        .breed("새종")
        .petType(PetType.BIRD)
        .name("앵무새")
        .petAge(3)
        .petGender(PetGender.MALE)
        .isNeutering(true)
        .chipNumber("12321-67890")
        .lostDate(LocalDate.of(2024, 10, 10))
        .lastSeenLocation("산")
        .lostStatus(LostStatus.LOST)
        .type("lost")
        .mapVisibility(MapVisibility.VISIBLE)
        .title("새 찾아주세요")
        .geography(changeToPoint(-73.968285, 40.785091))
        .description("설명")
        .contact("번호")
        .image("이미지")
        .member(testMember)
        .build();

    articleRepository.save(lostArticle);

    Article foundArticle = articleRepository.findById(lostArticle.getId()).orElseThrow();

    //when
    LostArticle updateLost = LostArticle.builder().title("바뀐 제목").build();
    foundArticle.applyChanges(updateLost);
    articleRepository.save(foundArticle);

    //then
    Article updatedArticle = articleRepository.findById(lostArticle.getId()).orElseThrow();
    assertThat(updatedArticle.getTitle()).isEqualTo("바뀐 제목");

  }

  // 삭제
  @Test
  @DisplayName("LostArticle 을 삭제할 수 있어야 한다.")
  void deleteLostArticle() {
    //given
    LostArticle lostArticle = LostArticle.builder()
        .breed("기타")
        .petType(PetType.OTHER)
        .name("냐용")
        .petAge(3)
        .petGender(PetGender.MALE)
        .isNeutering(false)
        .chipNumber("12321-67890")
        .lostDate(LocalDate.of(2024, 11, 10))
        .lastSeenLocation("편의점")
        .lostStatus(LostStatus.LOST)
        .type("lost")
        .mapVisibility(MapVisibility.VISIBLE)
        .title("고양이 찾아주세요")
        .geography(changeToPoint(-72.968285, 41.785091))
        .description("설명")
        .contact("번호")
        .image("이미지")
        .member(testMember)
        .build();

    Article savedArticle = articleRepository.save(lostArticle);

    //when
    articleRepository.deleteById(savedArticle.getId());

    //then
    assertThat(articleRepository.findById(savedArticle.getId())).isEmpty();

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


  private Point changeToPoint(Double longitude, Double latitude) {
    Point geography = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    geography.setSRID(4326);
    return geography;
  }


}