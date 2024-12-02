package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.*;
import com.mallangs.domain.image.entity.Image;
import com.mallangs.domain.image.repository.ImageRepository;
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
import org.springframework.test.annotation.Commit;
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
    private ImageRepository imageRepository;
    @Autowired
    private IsReadRepository isReadRepository;


    //회원, 주소, 채팅룸, 채팅방, 참여 채팅, 데이터 입력 1개씩
    @Test
    @Transactional
    @Commit
    public void 모든데이터값넣기() {
        // Member 저장
        Member member = memberRepository.save(Member.builder()
                .userId(new UserId("TtUser11"))
                .nickname(new Nickname("TestNickname"))
                .email(new Email("Tasdt@eami1s1l.com"))
                .password(new Password("1234aA!!", passwordEncoder))
                .hasPet(true)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .userId(new UserId("Te1dmnI1d"))
                .nickname(new Nickname("TestNickname2"))
                .email(new Email("Tes2@eami1l.com"))
                .password(new Password("1234aA!!", passwordEncoder))
                .memberRole(MemberRole.ROLE_ADMIN)
                .hasPet(false)
                .build());

        // ChatRoom 저장
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().build());

        // ParticipatedRoom 저장
        ParticipatedRoom participatedRoom = participatedRoomRepository.save(ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(member)
                .build());
        ParticipatedRoom participatedRoom2 = participatedRoomRepository.save(ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(member2)
                .build());

        chatRoom.addParticipatedRoom(participatedRoom);
        chatRoom.addParticipatedRoom(participatedRoom2);
        chatRoomRepository.save(chatRoom);

        // Image 저장
        Image savedImage1 = imageRepository.save(Image.builder()
                .url("qwerqaasdsd")
                .width(1234)
                .height(1234)
                .build());
        Image savedImage2 = imageRepository.save(Image.builder()
                .url("qwerea1asdsd")
                .width(12345)
                .height(12354)
                .build());

        // ChatMessage 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .participatedRoom(participatedRoom)
                .message("TestMessage")
                .sender(member)
                .messageImage(savedImage1)
                .build();
        chatMessageRepository.save(chatMessage);

        ChatMessage chatMessage2 = ChatMessage.builder()
                .participatedRoom(participatedRoom)
                .message("TestMessage2")
                .sender(member)
                .messageImage(savedImage2)
                .build();
        chatMessageRepository.save(chatMessage2);

        // IsRead 저장
        IsRead isRead1 = isReadRepository.save(IsRead.builder()
                        .chatMessage(chatMessage)
                .reader(chatRoom.getOccupiedRooms().get(0).getParticipant().getNickname().getValue())
                .build());
        IsRead isRead2 = isReadRepository.save(IsRead.builder()
                        .chatMessage(chatMessage2)
                .reader(chatRoom.getOccupiedRooms().get(1).getParticipant().getNickname().getValue())
                .build());

        chatMessage.addIsRead(isRead1);
        chatMessage.addIsRead(isRead2);
        chatMessageRepository.save(chatMessage); // 관계 저장

        chatMessage2.addIsRead(isRead1);
        chatMessage2.addIsRead(isRead2);
        chatMessageRepository.save(chatMessage2); // 관계 저장

        // Address 저장
        Address address = addressRepository.save(Address.builder()
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
                .build());
        Address address2 = addressRepository.save(Address.builder()
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
                .build());
        member.addAddress(address);
        member2.addAddress(address2);

        System.out.println("member.getAddresses().get(0).getAddressName() = " + member.getAddresses().get(0).getAddressName());
    }

}
