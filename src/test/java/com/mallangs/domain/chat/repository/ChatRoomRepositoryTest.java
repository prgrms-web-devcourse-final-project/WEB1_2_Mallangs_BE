package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.*;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ChatRoomRepositoryTest {

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
    AddressRepository addressRepository;

    @Test
    @Transactional
    public void test() {
//        given
        Member member = Member.builder()
                .userId(new UserId("TestUserId"))
                .nickname(new Nickname("TestNickname"))
                .email(new Email("Test@eamil.com"))
                .password(new Password("1234Tt!!", passwordEncoder))
                .hasPet(true)
                .build();
        memberRepository.save(member);

        Member member2 = Member.builder()
                .userId(new UserId("TestUserId2"))
                .nickname(new Nickname("TestNickname2"))
                .email(new Email("Test2@eamil.com"))
                .password(new Password("1234Tt!!", passwordEncoder))
                .hasPet(true)
                .build();
        memberRepository.save(member2);

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName("Test채팅방이름")
                .build();
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
                .chatRoom(chatRoom)
                .sender(member)
                .message("TestMessage")
                .type(MessageType.ENTER)
                .build();
        chatMessageRepository.save(chatMessage);
        ChatMessage chatMessage2 = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(member2)
                .message("TestMessage")
                .type(MessageType.ENTER)
                .build();
        chatMessageRepository.save(chatMessage2);
        participatedRoomRepository.save(participatedRoom);

        //when
        ChatRoom results = chatRoomRepository.findById(chatRoom.getChatRoomId()).get();

        //then
        assertNotNull(results);
//        assertEquals(results.getOccupiedRooms().get(0).getMessages().get(0).getSender(),"TestNickname");
//        assertEquals(results.getOccupiedRooms().get(1).getMessages().get(0).getSender(),"TestNickname2");

    }
}
