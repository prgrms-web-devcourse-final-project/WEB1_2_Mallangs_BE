//package com.mallangs.domain.community.repository;
//
//import com.mallangs.domain.community.entity.Category;
//import com.mallangs.domain.community.entity.CategoryStatus;
//import com.mallangs.domain.community.entity.Community;
//import com.mallangs.domain.community.entity.Likes;
//import com.mallangs.domain.member.entity.Address;
//import com.mallangs.domain.member.entity.Member;
//import com.mallangs.domain.member.entity.embadded.Email;
//import com.mallangs.domain.member.entity.embadded.Nickname;
//import com.mallangs.domain.member.entity.embadded.Password;
//import com.mallangs.domain.member.entity.embadded.UserId;
//import com.mallangs.domain.member.repository.AddressRepository;
//import com.mallangs.domain.member.repository.MemberRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.geo.Point;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@Transactional
//class LikesRepositoryTest {
//
//    @Autowired
//    private CommunityRepository communityRepository;
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
//    @Autowired
//    private LikesRepository likesRepository;
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
//                .point(new Point(1, 2))
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
//                .categoryStatus(CategoryStatus.ACTIVE)
//                .categoryOrder(1)
//                .build();
//        return categoryRepository.save(category);
//    }
//
//    private Community saveCommunity(Member member, Category category, String title, String content) {
//        Community community = Community.builder()
//                .member(member)
//                .category(category)
//                .title(title)
//                .content(content)
//                .build();
//        return communityRepository.save(community);
//    }
//
//    private Member testMember;
//    private Category testCategory;
//    private Community testCommunity;
//
//    @BeforeEach
//    void setUp() {
//        testMember = saveMember();
//        testCategory = saveCategory("일반게시판");
//        testCommunity = saveCommunity(testMember, testCategory, "테스트 제목", "테스트 내용");
//    }
//
//    @Test
//    @DisplayName("좋아요를 저장하고 조회")
//    void saveLikes() {
//        // given
//        Likes likes = new Likes(testMember, testCommunity);
//
//        // when
//        Likes savedLikes = likesRepository.save(likes);
//
//        // then
//        Optional<Likes> foundLikes = likesRepository.findById(savedLikes.getLikeId());
//        assertTrue(foundLikes.isPresent());
//        assertEquals(testMember.getMemberId(), foundLikes.get().getMember().getMemberId());
//        assertEquals(testCommunity.getBoardId(), foundLikes.get().getCommunity().getBoardId());
//    }
//
//}