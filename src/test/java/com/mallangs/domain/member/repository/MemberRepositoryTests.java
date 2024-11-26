package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.dto.MemberGetResponse;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.util.GeometryUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AddressRepository addressRepository;

    @Test
    public void findByUserIdTest() {
        //given
        Member member = Member.builder()
                .userId(new UserId("testId1234"))
                .password(new Password("122234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        Member foundMember = memberRepository.findByUserId(member.getUserId()).orElseThrow();
        Address foundAddress = addressRepository.findById(address.getId()).orElseThrow();
        //then
        assertThat(foundMember.getUserId().getValue()).isEqualTo("testId1234");
        assertThat(foundMember.getNickname().getValue()).isEqualTo("testname2");
        assertThat(foundMember.getEmail().getValue()).isEqualTo("test123@test.com");
        assertThat(foundMember.getHasPet()).isEqualTo(true);
        assertThat(foundMember.getAddresses().get(0).getAddressName()).isEqualTo(foundAddress.getAddressName());
    }

    @Test
    @Rollback
    public void existsByUserIdTest() {
        //given
        Member member = Member.builder()
                .userId(new UserId("testId123"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test12@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        Boolean isExist = memberRepository.existsByUserId(member.getUserId());
        //then
        assertThat(isExist).isTrue();

    }

    @Test
    @Rollback
    public void existsByEmailTest() {
        //given
        Member member = Member.builder()
                .userId(new UserId("testId1223"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test122@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        Boolean isExist = memberRepository.existsByEmail(member.getEmail());
        //then
        assertTrue(isExist);
    }

    @Test
    public void memberListTest() {
        //given
        Member member = Member.builder()
                .userId(new UserId("testId155"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test15221@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
                .build();
        memberRepository.save(member);

        Address address1 = Address.builder()
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

        Address address2 = Address.builder()
                .member(member)
                .addressName("testAddress2")
                .addressType("testAddressType2")
                .mainAddressNo("testmainAddressNo2")
                .point(GeometryUtil.createPoint(1, 2))
                .mountainYn("testMountainYn2")
                .region1depthName("testRegion1depthName2")
                .region2depthName("testRegion2depthName2")
                .region3depthHName("testRegion3depthHName2")
                .region3depthName("testRegion3depthName2")
                .mainBuildingNo("testMainBuildingNo2")
                .subBuildingNo("testSubBuildingNo2")
                .roadName("testRoadName2")
                .buildingName("testBuildingName2")
                .subAddressNo("testSubAddressNo2")
                .zoneNo("testZoneNo")
                .build();
        addressRepository.save(address1);
        addressRepository.save(address2);

        member.addAddress(address1);
        member.addAddress(address2);
        memberRepository.save(member);
        //when
        List<Member> members = memberRepository.memberList();
        List<MemberGetResponse> memberList = members.stream()
                .map(MemberGetResponse::new)
                .toList();
        //then
        assertEquals(memberList.get(0).getUserId(), "testId155");
        for (MemberGetResponse list : memberList) {
            log.info("list = {}", list.toString());
        }
    }
    @Test
    public void searchMemberByUserIdTest(){
//given
        Member member = Member.builder()
                .userId(new UserId("testId1234"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        List<Member> members = memberRepository.searchMemberByUserId("testI");
        //then
        assertNotNull(members);

        for (Member memberList : members){
            log.info("memberListByUserId = {}", memberList.toString());
        }
    }
    @Test
    public void searchMemberByEmailTest(){
        //given
        Member member = Member.builder()
                .userId(new UserId("testId1234"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        List<Member> members = memberRepository.searchMemberByEmail("test");
        //then
        assertNotNull(members);

        for (Member memberList : members){
            log.info("memberListByEmail = {}", memberList.toString());
        }
    }
    @Test
    public void searchMemberByNicknameTest(){
        //given
        Member member = Member.builder()
                .userId(new UserId("testId1234"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
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
        //when
        List<Member> members = memberRepository.searchMemberByNickname("test");
        //then
        assertNotNull(members);

        for (Member memberList : members){
            log.info("memberListByNickName = {}", memberList.toString());
        }
    }
}
