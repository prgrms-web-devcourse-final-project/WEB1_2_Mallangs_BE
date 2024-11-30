package com.mallangs.domain.chat.service;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import com.mallangs.domain.chat.dto.response.ChatMessageListResponse;
import com.mallangs.domain.chat.dto.response.ChatMessageResponse;
import com.mallangs.domain.chat.dto.request.UpdateChatMessageRequest;
import com.mallangs.domain.chat.dto.response.IsReadResponse;
import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.IsRead;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.chat.redis.RedisSubscriber;
import com.mallangs.domain.chat.repository.ChatMessageRepository;
import com.mallangs.domain.chat.repository.IsReadRepository;
import com.mallangs.domain.chat.repository.ParticipatedRoomRepository;
import com.mallangs.domain.image.dto.ImageResponse;
import com.mallangs.domain.image.entity.Image;
import com.mallangs.domain.image.repository.ImageRepository;
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
import java.util.Optional;

import static com.mallangs.domain.chat.entity.QIsRead.isRead;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ParticipatedRoomRepository participatedRoomRepository;
    private final RedisSubscriber redisSubscriber;
    private final ImageRepository imageRepository;
    private final IsReadRepository isReadRepository;

    //채팅 메세지 생성/송신
    public void sendMessage(ChatMessageRequest chatMessageRequest) {
        try {
            log.info("보내진 채팅 정보: {}", chatMessageRequest.toString());
            //참여 채팅 정보 추출
            ParticipatedRoom foundPartRoom = participatedRoomRepository.findByParticipatedRoomId(chatMessageRequest.getParticipatedRoomId())
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.PARTICIPATED_ROOM_NOT_FOUND));

            ChatRoom chatRoom = foundPartRoom.getChatRoom();

            //이미지 저장
            Image image = null;
            if (chatMessageRequest.getImageUrl() != null) {
                image = imageRepository.save(Image.builder().url(chatMessageRequest.getImageUrl()).width(chatMessageRequest.getWidth()).height(chatMessageRequest.getHeight()).build());
            }
            log.info("이미지 저장완료 보낸 채팅 정보: {}", image == null ? null : image.toString());

            //채팅메세지 저장
            ChatMessage chatMessage = ChatMessage.builder()
                    .participatedRoom(foundPartRoom)
                    .message(chatMessageRequest.getMessage())
                    .sender(foundPartRoom.getParticipant())
                    .messageImage(image).build();
            ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

            log.info("저장된 보낸 채팅 정보: {}", chatMessage.toString());

            // 읽음 표시 저장
            List<IsRead> isReadList = new ArrayList<>();
            for (ParticipatedRoom participatedRoom : chatRoom.getOccupiedRooms()) {
                String senderName = participatedRoom.getParticipant().getNickname().getValue();
                String ownerName = foundPartRoom.getParticipant().getNickname().getValue();
                IsRead isRead;

                //보낸사람의 메세지만 읽음 처리
                if (senderName.equals(ownerName)) {
                    isRead = IsRead.builder().chatMessage(savedChatMessage).sender(senderName).readCheck(true).build();
                } else {
                    isRead = IsRead.builder().chatMessage(savedChatMessage).sender(senderName).build();
                }
                IsRead savedIsRead = isReadRepository.save(isRead);
                isReadList.add(savedIsRead);
            }
            log.info(" 읽음 처리 완료 저장된 보낸 채팅 정보: {}", isReadList.toString());

            // 채팅에 보여지는 값
            ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                    .chatMessageId(savedChatMessage.getChatMessageId())
                    .chatRoomId(foundPartRoom.getChatRoom().getChatRoomId())
                    .message(savedChatMessage.getMessage())
                    .chatMessageImage(savedChatMessage.getMessageImage() == null ? null : new ImageResponse(savedChatMessage.getMessageImage()))
                    .sender(savedChatMessage.getSender().getNickname().getValue())
                    .profileImage(savedChatMessage.getSender().getProfileImage())
                    .type(savedChatMessage.getType())
                    .createTime(savedChatMessage.getCreatedAt())
                    .build();

            log.info("마지막 메세지 보낼 채팅 정보: {}", chatMessageResponse.toString());
            //채팅 보내기
            redisSubscriber.sendMessage(chatMessageResponse);
        } catch (Exception e) {
            throw new MallangsCustomException(ErrorCode.FAILED_CREATE_CHAT_MESSAGE);
        }
    }

    //채팅 수정
    public void update(UpdateChatMessageRequest chatMessageRequest) {

        ChatMessage foundChatMessage = chatMessageRepository.findByChatMessageId(chatMessageRequest.getChatMessageId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        foundChatMessage.changeMessage(chatMessageRequest.getMessage());
        chatMessageRepository.save(foundChatMessage);
    }

    //채팅 삭제
    public void delete(Long chatMessageId) {
        boolean isExist = chatMessageRepository.existsById(chatMessageId);
        if (isExist) {
            chatMessageRepository.deleteById(chatMessageId);
        } else throw new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
    }

    //채팅목록 페이징
    public Page<ChatMessageListResponse> getPage(PageRequestDTO pageRequestDTO, Long chatRoomId) { //목록
        try {
            //page, size, sort
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            log.info("쿼리 이전");

            // 채팅 마다 필요 데이터 DTO 매핑
            Page<ChatMessage> chatMessages = chatMessageRepository.findMessagesByChatRoomId(chatRoomId, pageable);

            log.info("리스트 중 채팅 조회 값: {}", chatMessages.toString());

            Page<ChatMessageListResponse> chatMessageResponse = chatMessages.map(
                    chatMessage -> ChatMessageListResponse.builder()
                            .chatMessageId(chatMessage.getChatMessageId())
                            .chatRoomId(chatMessage.getParticipatedRoom().getChatRoom().getChatRoomId())
                            .message(chatMessage.getMessage())
                            .chatMessageImage(chatMessage.getMessageImage() == null ? null : new ImageResponse(chatMessage.getMessageImage()))
                            .sender(chatMessage.getSender().getNickname().getValue())
                            .profileImage(chatMessage.getSender().getProfileImage())
                            .type(chatMessage.getType())
                            .isReadA(new IsReadResponse(chatMessage.getIsRead().get(0)))
                            .isReadB(new IsReadResponse(chatMessage.getIsRead().get(1)))
                            .build());

            log.info("채팅 이력 조회 값: {}", chatMessageResponse.toString());

            return chatMessageResponse;
        } catch (Exception e) {
            log.error("쳇서비스 페이징 실패 ={}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILED_GET_CHAT_MESSAGE);
        }
    }

    //가장 최근 읽음 처리된 메세지 부터, 찾아 읽음 처리하는 메서드
    public void changeUnReadToRead(Long chatMessageId) {
        if (chatMessageRepository.turnUnReadToRead(chatMessageId) < 0) {
            throw new MallangsCustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
        }
    }
}
