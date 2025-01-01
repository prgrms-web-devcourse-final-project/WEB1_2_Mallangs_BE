package com.mallangs.domain.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.chat.dto.response.ChatMessageResponse;
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

    //stomp (레디스 거치지 않고) 바로 메세지 전송
    public void sendMessage(ChatMessageResponse publishMessage) {
        try {
            messagingTemplate.convertAndSend("/sub/chat/room/" + publishMessage.getChatRoomId(), publishMessage);
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
        }
    }
    //publish Redis 메세지 수신
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            //레디스 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageResponse chatMessage = objectMapper.readValue(publishMessage, ChatMessageResponse.class);

            //stomp 이용해서 구독자들에게 메세지 송신
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("publish 레디스 메세지 -> stomp 메세지로 송신 실패 :{}", e.getMessage());
        }
    }
}

