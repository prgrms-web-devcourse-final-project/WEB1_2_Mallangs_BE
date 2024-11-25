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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private Community saveCommunity(Member member, Category category, String title, String content, String location,
                                    String imgUrl) {
        Community community = Community.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .build();
        return communityRepository.save(community);
    }

    @Test
    @DisplayName("게시글 생성 검증")
    void createCommunity() {
        // given
        Member member = saveMember();
        Category category = saveCategory("일반게시판");

        // when
        Community community = Community.builder()
                .member(member)
                .category(category)
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // then
        assertThat(community.getTitle()).isEqualTo("테스트 제목");
        assertThat(community.getContent()).isEqualTo("테스트 내용");
        assertThat(community.getCommunityStatus()).isEqualTo(CommunityStatus.PUBLISHED);
        assertThat(community.getViewCnt()).isZero();
        assertThat(community.getCommentCnt()).isZero();
        assertThat(community.getLikeCnt()).isZero();
    }

    @Test
    @DisplayName("게시글 수정")
    void change() {
        //given
        Member member = saveMember();
        Category category = saveCategory("실종게시판");

        Community community = Community.builder()
                .member(member)
                .category(category)
                .title("테스트 제목")
                .content("테스트 컨텐츠")
                .build();

        // when
        community.change("수정된 제목", "수정된 내용", "서울", "image.jpg");

        // then
        assertThat(community.getTitle()).isEqualTo("수정된 제목");
        assertThat(community.getContent()).isEqualTo("수정된 내용");
        assertThat(community.getLocation()).isEqualTo("서울");
        assertThat(community.getImgUrl()).isEqualTo("image.jpg");
    }
}