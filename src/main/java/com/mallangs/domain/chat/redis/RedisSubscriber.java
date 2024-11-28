package com.mallangs.domain.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.chat.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate redisTemplate;

    //메세지를 구독자들에게 송신
    public void sendMessage(ChatMessageResponse publishMessage) {
        try {
            messagingTemplate.convertAndSend("/sub/chat/room/" + publishMessage.getChatRoomId(), publishMessage);
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
        }
    }
    //stomp이용해서 구독자들에게 송신
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessageResponse chatMessage = objectMapper.readValue(publishMessage, ChatMessageResponse.class);

            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

