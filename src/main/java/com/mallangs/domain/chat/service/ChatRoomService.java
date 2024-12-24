package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.request.ChatRoomChangeNameRequest;
import com.mallangs.domain.chat.dto.response.ChatRoomResponse;
import com.mallangs.domain.chat.dto.response.ParticipatedRoomListResponse;
import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.chat.repository.ChatMessageRepository;
import com.mallangs.domain.chat.repository.ChatRoomRepository;
import com.mallangs.domain.chat.repository.ParticipatedRoomRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.notification.dto.NotificationSendDTO;
import com.mallangs.domain.notification.entity.NotificationType;
import com.mallangs.domain.notification.service.NotificationService;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.handler.SseEmitters;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final NotificationService notificationService;
    private final SseEmitters sseEmitters;

    // 채팅방 생성
    public Long create(Long myId, Long partnerId) {
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
                .roomName(partner.getNickname().getValue())
                .participant(me).build();
        participatedRoomRepository.save(participatedRoom1);

        //참여 채팅방 생성 - 상대방
        ParticipatedRoom participatedRoom2 = ParticipatedRoom.builder()
                .chatRoom(chatRoom)
                .roomName(me.getNickname().getValue())
                .participant(partner).build();
        participatedRoomRepository.save(participatedRoom2);

        // 알림 전송 - 상대방
        NotificationSendDTO notificationSendDTO = NotificationSendDTO.builder()
                .memberId(partnerId)
                .message("[" + me.getNickname().getValue() + "]님이 새로운 채팅방을 개설")
                .notificationType(NotificationType.CHAT)
                .url("/chat-room/" + savedChatRoom.getChatRoomId())
                .build();
        notificationService.send(notificationSendDTO);

        String emitterId = partnerId + "_";
        SseEmitter emitter = sseEmitters.findSingleEmitter(emitterId);

        if (emitter != null) {
            try {
                ChatRoomResponse chatRoomResponse = ChatRoomResponse.builder()
                        .chatRoomName(partner.getNickname().getValue())
                        .memberNickname(me.getNickname().getValue())
                        .memberId(me.getMemberId())
                        .changedIsRead(0)
                        .build();
                emitter.send(chatRoomResponse);
            } catch (IOException e) {
                log.error("Error sending chat room notification to client via SSE: {}", e.getMessage());
                sseEmitters.delete(emitterId);
            }
        }

        return savedChatRoom.getChatRoomId();
    }

    //채팅방 리스트 조회
    public List<ParticipatedRoomListResponse> get(Long memberId) {
        List<ParticipatedRoomListResponse> chatRooms = new ArrayList<>();

        //참여 채팅에서 정보 추출 & 조합 -> DTO 입력
        List<ParticipatedRoom> rooms = participatedRoomRepository.findByMemberId(memberId);
        for (ParticipatedRoom room : rooms) {

            List<ChatMessage> messages = chatMessageRepository.findMessageByChatRoomId(
                    room.getChatRoom().getChatRoomId());

            List<ChatMessage> unreadMessages = new ArrayList<>();
            for (ChatMessage message : messages) {
                if (!message.getReceiverRead()) {
                    unreadMessages.add(message);
                    break;
                }
            }

            ParticipatedRoomListResponse info = ParticipatedRoomListResponse.builder()
                    .participatedRoomId(room.getParticipatedRoomId())
                    .chatRoomId(room.getChatRoom().getChatRoomId())
                    .nickname(room.getParticipant().getNickname().getValue())
                    .message(messages.isEmpty() ? null : messages.get(0).getMessage())
                    .chatRoomName(room.getRoomName())
                    .lastChatTime(messages.isEmpty() ? null : messages.get(0).getCreatedAt())
                    .notReadCnt(unreadMessages.size())
                    .build();
            log.info("room정보1 :{}", room.getParticipatedRoomId());
            log.info("room정보2 :{}", room.getChatRoom().getChatRoomId());
            log.info("room정보3 :{}", room.getParticipant().getNickname());

            chatRooms.add(info);
        }

        return chatRooms;
    }

    //채팅방 이름 변경
    public boolean update(ChatRoomChangeNameRequest chatRoomChangeNameRequest) {
        try {
            ChatRoom chatRoom = chatRoomRepository.findById(chatRoomChangeNameRequest.getChatRoomId())
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHATROOM_NOT_FOUND));

            chatRoom.changeChatRoomName(chatRoomChangeNameRequest.getRoomName());
            chatRoomRepository.save(chatRoom);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILED_UPDATE_CHAT_ROOM);
        }
    }

    //채팅방 삭제
    public void delete(String userId, Long participatedRoomId) {
        ParticipatedRoom partRoom = participatedRoomRepository.findByPRoomId(participatedRoomId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

        if (partRoom.getParticipant().getUserId().getValue().equals(userId)) {
            participatedRoomRepository.deleteById(participatedRoomId);
        } else {
            throw new MallangsCustomException(ErrorCode.FAILED_DELETE_CHATROOM);
        }
    }

}
