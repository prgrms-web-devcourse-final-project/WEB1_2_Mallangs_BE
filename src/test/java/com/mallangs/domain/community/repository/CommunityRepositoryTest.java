package com.mallangs.domain.community.repository;

import com.mallangs.domain.community.entity.Category;
import com.mallangs.domain.community.entity.CategoryStatus;
import com.mallangs.domain.community.entity.Community;
import com.mallangs.domain.community.entity.CommunityStatus;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommunityRepositoryTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    private Member saveMember() {
        Member member = Member.builder()
                .userId(new UserId("TestUser1"))
                .nickname(new Nickname("JohnDoe"))
                .email(new Email("john@doe.com"))
                .password(new Password("1Q2w3e4r!", passwordEncoder))
                .hasPet(false)
                .build();
        memberRepository.save(member);

        Address address = Address.builder()
                .member(member)
                .addressName("testAddress")
                .addressType("testAddressType")
                .mainAddressNo("testmainAddressNo")
                .point(new Point(1, 2))
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
        return memberRepository.save(member);
    }

    private Category saveCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .categoryStatus(CategoryStatus.ACTIVE)
                .categoryOrder(1)
                .build();
        return categoryRepository.save(category);
    }

    private Community saveCommunity(Member member, Category category, String title, String content) {
        Community community = Community.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .build();
        return communityRepository.save(community);
    }

    private Member testMember;
    private Category testCategory;
    private Community testCommunity;

    @BeforeEach
    void setUp() {
        testMember = saveMember();
        testCategory = saveCategory("일반게시판");
        testCommunity = saveCommunity(testMember, testCategory, "테스트 제목", "테스트 내용");
    }

    @Test
    @DisplayName("카테고리별 게시글 조회")
    void findByCategoryId() {
        // when
        Page<Community> result = communityRepository.findByCategoryId(testCategory.getCategoryId(), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("키워드로 게시글 검색")
    void searchByTitleOrContent() {
        // when
        Page<Community> result = communityRepository.searchByTitleOrContent("제목", PageRequest.of(0, 10));

        // then
        Community foundCommunity = result.getContent().get(0);
        assertThat(foundCommunity.getTitle()).contains("제목");
        assertThat(foundCommunity.getContent()).contains("내용");
    }

    @Test
    @DisplayName("회원별 게시글 조회")
    void findByMemberId() {
        // when
        Page<Community> result = communityRepository.findByMemberId(testMember.getMemberId(), PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getMember().getMemberId()).isEqualTo(testMember.getMemberId());
    }

    @Test
    @DisplayName("상태별 게시글 조회")
    void findByStatus() {
        // when
        Page<Community> result = communityRepository.findByStatus(CommunityStatus.PUBLISHED, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCommunityStatus()).isEqualTo(CommunityStatus.PUBLISHED);
    }

    @Test
    @DisplayName("카테고리와 제목으로 게시글 검색")
    void searchForAdmin() {
        // when
        Page<Community> result = communityRepository.searchForAdmin(testCategory.getCategoryId(), "테스트", PageRequest.of(0, 10));

        // then
        Community foundCommunity = result.getContent().get(0);
        assertThat(foundCommunity.getCategory().getCategoryId()).isEqualTo(testCategory.getCategoryId());
        assertThat(foundCommunity.getTitle()).contains("테스트");
    }
}