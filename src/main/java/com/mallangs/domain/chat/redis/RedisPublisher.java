package com.mallangs.domain.chat.redis;

import com.mallangs.domain.chat.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    //레디스함 채널명
    private static final String CHAT_CHANNEL = "chat_channel";

    //레디스함으로 메세지 송신
    public void publish(ChatMessageResponse message) {
        redisTemplate.convertAndSend(CHAT_CHANNEL, message);
    }
}
