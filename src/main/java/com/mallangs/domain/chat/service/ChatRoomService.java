package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.request.ChatRoomChangeNameRequest;
import com.mallangs.domain.chat.dto.response.ParticipatedRoomListResponse;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;

    // 채팅방 생성
    public Long create(Long myId,Long partnerId) {
        //채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder().build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //회원정보 입력 - 나
        Member me = memberRepository.findById(myId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        //회원정보 입력 - 상대방
        Member partner = memberRepository.findById(partnerId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        //참여 채팅방 생성 - 나
        ParticipatedRoom participatedRoom1 = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(me).build();
        participatedRoomRepository.save(participatedRoom1);

        //참여 채팅방 생성 - 상대방
        ParticipatedRoom participatedRoom2 = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .participant(partner).build();
        participatedRoomRepository.save(participatedRoom2);

        return savedChatRoom.getChatRoomId();
    }

    //채팅방 조회
    public List<ParticipatedRoomListResponse> get(Long memberId) {
        List<ParticipatedRoomListResponse> chatRooms = new ArrayList<>();

        //참여 채팅에서 정보 추출 & 조합 -> DTO 입력
        List<ParticipatedRoom> rooms = participatedRoomRepository.findByMemberId(memberId);
        for (ParticipatedRoom room : rooms) {
            //메세지 0번째 -> 가장 최근 메세지
            Collections.reverse(room.getMessages());
            ParticipatedRoomListResponse info = ParticipatedRoomListResponse.builder()
                    .participatedRoomId(room.getParticipatedRoomId())
                    .chatRoomId(room.getChatRoom().getChatRoomId())
                    .nickname(room.getParticipant().getNickname())
                    .message(room.getMessages().isEmpty() ? null:room.getMessages().get(0).getMessage())
                    .createdAt(room.getMessages().isEmpty()? null:room.getMessages().get(0).getCreatedAt())
                    .build();
            log.info("room정보1 :{}", room.getParticipatedRoomId());
            log.info("room정보2 :{}", room.getChatRoom().getChatRoomId());
            log.info("room정보3 :{}", room.getParticipant().getNickname());
            if (!room.getMessages().isEmpty()) {
                log.info("room정보4 :{}", room.getMessages().get(0).getMessage());
                log.info("room정보5 :{}", room.getMessages().get(0).getCreatedAt());
            } else {
                log.info("room정보4 : 메시지가 없습니다.");
                log.info("room정보5 : 메시지가 없습니다.");
            }

            chatRooms.add(info);
        }
        return chatRooms;
    }

    //채팅방 이름 변경
    public void update(ChatRoomChangeNameRequest chatRoomChangeNameRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomChangeNameRequest.getChatRoomId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHATROOM_NOT_FOUND));

        chatRoom.changeChatRoomName(chatRoomChangeNameRequest.getRoomName());
        chatRoomRepository.save(chatRoom);
    }

    //채팅방 삭제
    public void delete(String userId, Long participatedRoomId) {
        ParticipatedRoom partRoom = participatedRoomRepository.findByPRoomId(participatedRoomId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

        if (partRoom.getParticipant().getUserId().getValue().equals(userId)) {
            participatedRoomRepository.deleteById(participatedRoomId);
        } else throw new MallangsCustomException(ErrorCode.FAILED_DELETE_CHATROOM);
    }
}
