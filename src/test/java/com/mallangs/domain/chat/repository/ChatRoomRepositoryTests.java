package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.*;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.util.GeometryUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatRoomRepositoryTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    ParticipatedRoomRepository participatedRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    IsReadRepository isReadRepository;

    @Test
    //회원, 주소, 채팅룸, 채팅방, 참여 채팅, 데이터 입력 1개씩
    @Transactional
    public void 모든데이터값넣기(){
        Member member = Member.builder()
                .userId(new UserId("TestUserId"))
                .nickname(new Nickname("TestNickname"))
                .email(new Email("Test@eamil.com"))
                .password(new Password("1234aA!!", passwordEncoder))
                .hasPet(true)
                .build();
        memberRepository.save(member);

        Member member2 = Member.builder()
                .userId(new UserId("TestAdminId"))
                .nickname(new Nickname("TestNickname2"))
                .email(new Email("Test2@eamil.com"))
                .password(new Password("1234aA!!", passwordEncoder))
                .memberRole(MemberRole.ROLE_ADMIN)
                .hasPet(false)
                .build();
        memberRepository.save(member2);

        ChatRoom chatRoom = ChatRoom.builder().build();
        chatRoomRepository.save(chatRoom);

        ParticipatedRoom participatedRoom = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(member)
                .build();
        ParticipatedRoom participatedRoom2 = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(member2)
                .build();
        participatedRoomRepository.save(participatedRoom);
        participatedRoomRepository.save(participatedRoom2);
        chatRoom.addParticipatedRoom(participatedRoom);
        chatRoom.addParticipatedRoom(participatedRoom2);
        chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage = ChatMessage.builder()
                .participatedRoom(participatedRoom)
                .sender(member)
                .message("TestMessage")
                .type(MessageType.ENTER)
                .build();
        ChatMessage chatMessage2 = ChatMessage.builder()
                .participatedRoom(participatedRoom2)
                .sender(member2)
                .message("TestMessage")
                .type(MessageType.ENTER)
                .build();
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.save(chatMessage2);
        participatedRoom.addChatMessage(chatMessage);
        participatedRoom2.addChatMessage(chatMessage2);
        participatedRoomRepository.save(participatedRoom);

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
        Address address2 = Address.builder()
                .member(member2)
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
        addressRepository.save(address2);
        member.addAddress(address);
        member2.addAddress(address2);

        IsRead isRead = IsRead.builder()
                .chatMessage(chatMessage)
                .sender(member.getNickname().getValue()).build();
        isReadRepository.save(isRead);
        IsRead isRead2 = IsRead.builder()
                .chatMessage(chatMessage2)
                .sender(member2.getNickname().getValue()).build();
        isReadRepository.save(isRead2);

        System.out.println("member.getAddresses().get(0).getAddressName() = " + member.getAddresses().get(0).getAddressName());
    }
}
