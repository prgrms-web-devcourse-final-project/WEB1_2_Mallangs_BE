package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import com.mallangs.domain.chat.dto.response.ChatMessageResponse;
import com.mallangs.domain.chat.dto.request.UpdateChatMessageRequest;
import com.mallangs.domain.chat.entity.ChatMessage;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;
    private final RedisSubscriber redisSubscriber;

    //채팅 메세지 생성/송신
    public void sendMessage(ChatMessageRequest chatMessageRequest) {
        try {
        //참여 채팅 정보 추출
        ParticipatedRoom foundPartRoom = participatedRoomRepository.findByParticipatedRoomId(chatMessageRequest.getParticipatedRoomId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder()
                .participatedRoom(foundPartRoom)
                .message(chatMessageRequest.getMessage())
                .sender(foundPartRoom.getParticipant())
                .imageUrl(chatMessageRequest.getImageUrl()).build();
        chatMessageRepository.save(chatMessage);

        // 채팅에 보여지는 값
        ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .chatRoomId(foundPartRoom.getChatRoom().getChatRoomId())
                .message(chatMessage.getMessage())
                .imageUrl(chatMessage.getImageUrl())
                .sender(chatMessage.getSender().getNickname().getValue())
                .profileImage(chatMessage.getSender().getProfileImage())
                .type(chatMessage.getType())
                .isRead(chatMessage.getIsRead()).build();

        //채팅 보내기
        redisSubscriber.sendMessage(chatMessageResponse);
        }catch (Exception e){
            throw new MallangsCustomException(ErrorCode.FAILED_CREATE_CHAT_MESSAGE);
        }
    }

    //채팅 수정
    public ChatMessageResponse update(UpdateChatMessageRequest chatMessageRequest) {
        ChatMessage foundChatMessage = chatMessageRepository.findByChatMessageId(chatMessageRequest.getChatMessageId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        foundChatMessage.changeMessage(chatMessageRequest.getMessage());
        return ChatMessageResponse.builder()
                .chatMessageId(foundChatMessage.getChatMessageId())
                .chatRoomId(foundChatMessage.getParticipatedRoom().getChatRoom().getChatRoomId())
                .message(foundChatMessage.getMessage())
                .sender(foundChatMessage.getSender().getNickname().getValue())
                .profileImage(foundChatMessage.getSender().getProfileImage())
                .imageUrl(foundChatMessage.getImageUrl())
                .type(foundChatMessage.getType())
                .isRead(foundChatMessage.getIsRead()).build();
    }

    //채팅 삭제
    public void delete(Long chatMessageId) {
        boolean isExist = chatMessageRepository.existsById(chatMessageId);
        if (isExist){
            chatMessageRepository.deleteById(chatMessageId);
        }else throw new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
    }

    //채팅목록 페이징
    public Page<ChatMessageResponse> getPage(PageRequestDTO pageRequestDTO, Long chatRoomId) { //목록
        try {
            List<ChatMessageResponse> chatList = new ArrayList<>();

            //page, size, sort
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            // 채팅 마다 필요 데이터 DTO 매핑
            List<ChatMessage> chatMessages = chatMessageRepository.findMessagesByChatRoomId(chatRoomId);
            for (ChatMessage chatMessage : chatMessages ){
                ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                        .chatMessageId(chatMessage.getChatMessageId())
                        .chatRoomId(chatMessage.getParticipatedRoom().getChatRoom().getChatRoomId())
                        .message(chatMessage.getMessage())
                        .sender(chatMessage.getSender().getNickname().getValue())
                        .profileImage(chatMessage.getSender().getProfileImage())
                        .imageUrl(chatMessage.getImageUrl())
                        .type(chatMessage.getType())
                        .isRead(chatMessage.getIsRead()).build();

                chatList.add(chatMessageResponse);
            }

            //페이지 시작, 끝 만들기
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), chatList.size());
            List<ChatMessageResponse> chatMessagePage = chatList.subList(start, end);

            return new PageImpl<>(chatMessagePage, pageable, chatList.size());
        } catch (Exception e) {
            log.error("쳇서비스 페이징 실패 ={}",e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILED_GET_CHAT_MESSAGE);
        }
    }
}
