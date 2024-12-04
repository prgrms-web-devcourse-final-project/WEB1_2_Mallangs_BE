package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import com.mallangs.domain.chat.dto.request.UpdateChatMessageRequest;
import com.mallangs.domain.chat.dto.response.*;
import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.chat.redis.RedisSubscriber;
import com.mallangs.domain.chat.repository.ChatMessageRepository;
import com.mallangs.domain.chat.repository.ParticipatedRoomRepository;
import com.mallangs.domain.member.dto.PageRequestDTO;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;
    private final RedisSubscriber redisSubscriber;

    //채팅 메세지 생성/송신
    public ChatMessageSuccessResponse sendMessage(ChatMessageRequest chatMessageRequest) {
        try {
            log.info("보내진 채팅 정보: {}", chatMessageRequest.toString());

            //참여 채팅 정보 추출
            ParticipatedRoom foundPartRoom = participatedRoomRepository.findByParticipatedRoomId(chatMessageRequest.getParticipatedRoomId())
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

            ChatRoom chatRoom = foundPartRoom.getChatRoom();

            //채팅메세지 저장
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoom(chatRoom)
                    .message(chatMessageRequest.getMessage())
                    .sender(foundPartRoom.getParticipant())
                    .senderRead(true)
                    .receiverRead(false)
                    .imageUrl(chatMessageRequest.getImageUrl()).build();
            ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
            log.info("저장된 보낸 채팅 정보: {}", chatMessage.toString());

            // 채팅에 보여지는 값
            ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                    .chatMessageId(savedChatMessage.getChatMessageId())
                    .chatRoomId(chatRoom.getChatRoomId())
                    .message(savedChatMessage.getMessage())
                    .chatMessageImage(savedChatMessage.getImageUrl())
                    .sender(savedChatMessage.getSender().getNickname().getValue())
                    .type(savedChatMessage.getType())
                    .createTime(savedChatMessage.getCreatedAt())
                    .build();

            log.info("마지막 메세지 보낼 채팅 정보: {}", chatMessageResponse.toString());

            //채팅 보내기
            redisSubscriber.sendMessage(chatMessageResponse);
            return new ChatMessageSuccessResponse(savedChatMessage.getSender().getUserId().getValue());
        } catch (Exception e) {
            throw new MallangsCustomException(ErrorCode.FAILED_CREATE_CHAT_MESSAGE);
        }
    }

    //채팅 수정
    public ChatMessageToDTOResponse update(UpdateChatMessageRequest chatMessageRequest) {

        ChatMessage foundChatMessage = chatMessageRepository.findByChatMessageId(chatMessageRequest.getChatMessageId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        foundChatMessage.changeMessage(chatMessageRequest.getMessage());
        ChatMessage save = chatMessageRepository.save(foundChatMessage);
        return new ChatMessageToDTOResponse(save);
    }

    //채팅 삭제
    public ChatMessageDeleteSuccessResponse delete(Long chatMessageId) {
        boolean isExist = chatMessageRepository.existsById(chatMessageId);
        if (isExist) {
            chatMessageRepository.deleteById(chatMessageId);
            return new ChatMessageDeleteSuccessResponse(chatMessageId);
        } else throw new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
    }

    //채팅목록 페이징
    public Page<ChatMessageListResponse> getPage(PageRequestDTO pageRequestDTO, Long chatRoomId) { //목록

        //page, size, sort
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        log.info("쿼리 이전");

        // 채팅 마다 필요 데이터 DTO 매핑
        Page<ChatMessage> chatMessages = chatMessageRepository.findMessagesByChatRoomId(chatRoomId, pageable);
        if (chatMessages.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
        }

        try {
            log.info("리스트 중 채팅 조회 값: {}", chatMessages.toString());

            Page<ChatMessageListResponse> chatMessageResponse = chatMessages.map(
                    chatMessage -> ChatMessageListResponse.builder()
                            .chatMessageId(chatMessage.getChatMessageId())
                            .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                            .message(chatMessage.getMessage())
                            .chatMessageImage(chatMessage.getImageUrl())
                            .sender(chatMessage.getSender().getNickname().getValue())
                            .profileImage(chatMessage.getSender().getProfileImage())
                            .type(chatMessage.getType())
                            .senderRead(chatMessage.getSenderRead())
                            .receiverRead(chatMessage.getSenderRead())
                            .build());

            log.info("채팅 이력 조회 값: {}", chatMessageResponse.toString());

            return chatMessageResponse;
        } catch (Exception e) {
            log.error("쳇서비스 페이징 실패 ={}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILED_GET_CHAT_MESSAGE);
        }
    }

    //가장 최근 읽음 처리된 메세지 부터, 찾아 읽음 처리하는 메서드
    public ChatRoomResponse changeUnReadToRead(Long participatedRoomId, String nickname) {

        //참여 채팅 정보 추출
        ParticipatedRoom foundPartRoom = participatedRoomRepository.findByParticipatedRoomId(participatedRoomId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

        ChatRoom chatRoom = foundPartRoom.getChatRoom();
        if (chatRoom == null) {
            throw new MallangsCustomException(ErrorCode.CHATROOM_NOT_FOUND);
        }

        int numChanged = chatMessageRepository.updateRead(chatRoom.getChatRoomId(), nickname);

        //dto 로 변경
        return ChatRoomResponse.builder()
                .chatRoomName(foundPartRoom.getRoomName())
                .memberNickname(foundPartRoom.getParticipant().getNickname().getValue())
                .memberProfileUrl(foundPartRoom.getParticipant().getProfileImage())
                .changedIsRead(numChanged)
                .build();
    }
}
