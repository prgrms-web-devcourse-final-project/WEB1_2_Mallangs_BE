//package com.mallangs.domain.board.repository;
//
//import com.mallangs.domain.board.entity.*;
//import com.mallangs.domain.member.entity.Address;
//import com.mallangs.domain.member.entity.Member;
//import com.mallangs.domain.member.entity.embadded.Email;
//import com.mallangs.domain.member.entity.embadded.Nickname;
//import com.mallangs.domain.member.entity.embadded.Password;
//import com.mallangs.domain.member.entity.embadded.UserId;
//import com.mallangs.domain.member.repository.AddressRepository;
//import com.mallangs.domain.member.repository.MemberRepository;
//import com.mallangs.domain.member.util.GeometryUtil;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class BoardRepositoryTest {
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    private Member saveMember() {
//        Member member = Member.builder()
//                .userId(new UserId("TestUser1"))
//                .nickname(new Nickname("JohnDoe"))
//                .email(new Email("john@doe.com"))
//                .password(new Password("1Q2w3e4r!", passwordEncoder))
//                .hasPet(false)
//                .build();
//        memberRepository.save(member);
//
//        Address address = Address.builder()
//                .member(member)
//                .addressName("testAddress")
//                .addressType("testAddressType")
//                .mainAddressNo("testmainAddressNo")
//                .point(GeometryUtil.createPoint(1, 2))
//                .mountainYn("testMountainYn")
//                .region1depthName("testRegion1depthName")
//                .region2depthName("testRegion2depthName")
//                .region3depthHName("testRegion3depthHName")
//                .region3depthName("testRegion3depthName")
//                .mainBuildingNo("testMainBuildingNo")
//                .subBuildingNo("testSubBuildingNo")
//                .roadName("testRoadName")
//                .buildingName("testBuildingName")
//                .subAddressNo("testSubAddressNo")
//                .zoneNo("testZoneNo")
//                .build();
//        addressRepository.save(address);
//        member.addAddress(address);
//        return memberRepository.save(member);
//    }
//
//    private Category saveCategory(String name) {
//        Category category = Category.builder()
//                .name(name)
//                .description("테스트 카테고리 설명")
//                .categoryLevel(CategoryLevel.SUB_LEVEL)
//                .categoryOrder(1)
//                .build();
//        return categoryRepository.save(category);
//    }
//
//    private Board saveCommunity(Member member, Category category, String title, String content) {
//        Board board = Board.builder()
//                .member(member)
//                .category(category)
//                .title(title)
//                .content(content)
//                .latitude(new BigDecimal("37.5665"))    // 서울시청 위도
//                .longitude(new BigDecimal("126.9780"))  // 서울시청 경도
//                .address("서울특별시 중구 세종대로 110")
//                .sightedAt(LocalDateTime.now())
//                .imageId(1L)
//                .boardType(BoardType.COMMUNITY)
//                .build();
//        return boardRepository.save(board);
//    }
//
//    private Member testMember;
//    private Category testCategory;
//    private Board testBoard;
//
//    @BeforeEach
//    void setUp() {
//        testMember = saveMember();
//        testCategory = saveCategory("일반게시판");
//        testBoard = saveCommunity(testMember, testCategory, "테스트 제목", "테스트 내용");
//    }
//
//    @Test
//    @DisplayName("카테고리별 게시글 조회 - PUBLISH만")
//    void findByCategoryId() {
//        // given
//        // DRAFT 상태의 게시글 생성
//        Board draftBoard = saveCommunity(testMember, testCategory, "임시저장 게시글", "임시저장 내용");
//        draftBoard.changeStatus(BoardStatus.DRAFT);
//        boardRepository.save(draftBoard);
//
//        // when
//        Page<Board> result = boardRepository.findByCategoryId(testCategory.getCategoryId(), BoardType.COMMUNITY, PageRequest.of(0, 10));
//
//        // then
//        assertThat(result.getContent()).hasSize(1);  // PUBLISH 상태의 게시글만 조회
//        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 제목");
//        assertThat(result.getContent().get(0).getBoardStatus()).isEqualTo(BoardStatus.PUBLISHED);
//    }
//
//    @Test
//    @DisplayName("키워드로 게시글 검색 - PUBLISHED 상태만 검색되어야 함")
//    void searchByTitleOrContent() {
//        // given
//        Board draftBoard = saveCommunity(testMember, testCategory, "임시저장 테스트", "임시저장 내용");
//        draftBoard.changeStatus(BoardStatus.DRAFT);
//        boardRepository.save(draftBoard);
//
//        // when
//        Page<Board> result = boardRepository.searchByTitleOrContent("테스트", BoardType.COMMUNITY,PageRequest.of(0, 10));
//
//        // then
//        assertThat(result.getContent()).hasSize(1);  // PUBLISHED 상태의 게시글만 조회
//        Board foundBoard = result.getContent().get(0);
//        assertThat(foundBoard.getTitle()).contains("테스트");
//        assertThat(foundBoard.getBoardStatus()).isEqualTo(BoardStatus.PUBLISHED);
//    }
//
//    @Test
//    @DisplayName("회원별 게시글 조회 - PUBLISHED 상태만 조회되어야 함")
//    void findByMemberId() {
//        // given
//        Board hiddenBoard = saveCommunity(testMember, testCategory, "숨김 게시글", "숨김 내용");
//        hiddenBoard.changeStatus(BoardStatus.HIDDEN);
//        boardRepository.save(hiddenBoard);
//
//        // when
//        Page<Board> result = boardRepository.findByMemberId(
//                testMember.getMemberId(),
//                BoardType.COMMUNITY,
//                PageRequest.of(0, 10)
//        );
//
//        // then
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getContent().get(0).getBoardStatus()).isEqualTo(BoardStatus.PUBLISHED);
//    }
//
//    @Test
//    @DisplayName("상태별 게시글 조회")
//    void findByStatus() {
//        // given
//        Board draftBoard = saveCommunity(testMember, testCategory, "임시저장", "임시저장");
//        draftBoard.changeStatus(BoardStatus.DRAFT);
//        boardRepository.save(draftBoard);
//
//        // when
//        Page<Board> publishedResult = boardRepository.findByStatus(
//                BoardStatus.PUBLISHED,
//                PageRequest.of(0, 10)
//        );
//        Page<Board> draftResult = boardRepository.findByStatus(
//                BoardStatus.DRAFT,
//                PageRequest.of(0, 10)
//        );
//
//        // then
//        assertThat(publishedResult.getContent()).hasSize(1);
//        assertThat(draftResult.getContent()).hasSize(1);
//    }
//

////    @Test
////    @DisplayName("관리자용 - 카테고리와 제목으로 게시글 검색")
////    void searchForAdmin() {
////        // given
////        Board hiddenBoard = saveCommunity(testMember, testCategory, "테스트 숨김", "숨김 내용");
////        hiddenBoard.changeStatus(BoardStatus.HIDDEN);
////        boardRepository.save(hiddenBoard);
////
////        // when
////        Page<Board> result = boardRepository.searchForAdmin(
////                testCategory.getCategoryId(),
////                "테스트",
////                PageRequest.of(0, 10)
////        );
////
////        // then
////        assertThat(result.getContent()).hasSize(2);  // 모든 상태의 게시글이 검색됨
////        assertThat(result.getContent()).extracting("title")
////                .containsExactlyInAnyOrder("테스트 제목", "테스트 숨김");
////    }
////
////    @Test
////    @DisplayName("관리자용 - 카테고리, 상태, 제목으로 게시글 검색")
////    void searchForAdminWithStatus() {
////        // given
////        Board hiddenBoard = saveCommunity(testMember, testCategory, "테스트 숨김", "숨김 내용");
////        hiddenBoard.changeStatus(BoardStatus.HIDDEN);
////        boardRepository.save(hiddenBoard);
////
////        // when
////        Page<Board> result = boardRepository.searchForAdminWithStatus(
////                testCategory.getCategoryId(),
////                BoardStatus.HIDDEN,
////                "테스트",
////                PageRequest.of(0, 10)
////        );
////
////        // then
////        assertThat(result.getContent()).hasSize(1);
////        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 숨김");
////        assertThat(result.getContent().get(0).getBoardStatus()).isEqualTo(BoardStatus.HIDDEN);
////    }
//}