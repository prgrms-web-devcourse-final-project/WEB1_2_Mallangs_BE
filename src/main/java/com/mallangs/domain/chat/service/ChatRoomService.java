package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.ChatRoomChangeNameRequest;
import com.mallangs.domain.chat.dto.ParticipatedRoomListResponse;
import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.chat.repository.ChatRoomRepository;
import com.mallangs.domain.chat.repository.ParticipatedRoomRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;

    // 채팅방 생성
    public ChatRoom createChatRoom(Long participantId) {
        //채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder().build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //회원정보 입력
        Member member = memberRepository.findById(participantId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        //참여 채팅방 생성
        ParticipatedRoom participatedRoom = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(member).build();
        participatedRoomRepository.save(participatedRoom);

        return savedChatRoom;
    }

    //채팅방 조회
    public List<ParticipatedRoomListResponse> getChatRoom(Long memberId) {
        List<ParticipatedRoomListResponse> chatRooms = new ArrayList<>();

        //참여 채팅에서 정보 추출 & 조합 -> DTO 입력
        List<ParticipatedRoom> rooms = participatedRoomRepository.findByMemberId(memberId);
        for (ParticipatedRoom room : rooms) {
            ParticipatedRoomListResponse info = ParticipatedRoomListResponse.builder()
                    .participatedRoomId(room.getParticipatedRoomId())
                    .chatRoomId(room.getChatRoom().getChatRoomId())
                    .nickname(room.getParticipant().getNickname())
                    .message(room.getMessages().get(0).getMessage())
                    .createdAt(room.getMessages().get(0).getCreatedAt()).build();
            chatRooms.add(info);
        }
        return chatRooms;
    }

    //채팅방 이름 변경
    public void changeChatRoomName(ChatRoomChangeNameRequest chatRoomChangeNameRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomChangeNameRequest.getChatRoomId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHATROOM_NOT_FOUND));

        chatRoom.changeChatRoomName(chatRoomChangeNameRequest.getRoomName());
        chatRoomRepository.save(chatRoom);
    }

    //채팅방 삭제
    public void delete(UserId userId, Long participatedRoomId) {
        ParticipatedRoom partRoom = participatedRoomRepository.findById(participatedRoomId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));
        if (partRoom.getParticipant().getUserId().equals(userId)) {
            participatedRoomRepository.deleteById(participatedRoomId);
        } else throw new MallangsCustomException(ErrorCode.FAILED_DELETE_CHATROOM);
    }
}
