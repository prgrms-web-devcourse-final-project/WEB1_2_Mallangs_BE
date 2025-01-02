package com.mallangs.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.chat.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@Log4j2
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    //Redis 채널명
    public ChannelTopic topicPattern() {
        return new ChannelTopic("chat_channel");
    }

    //클라이언트로 부터 메세지 수신
    @Bean
    //Redis 메세지 구독을 담당
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory
            ,MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory); //연결될 레디스 서버 주소, 포트 설정(매핑)
        container.addMessageListener(listenerAdapter, topicPattern()); //연결될 레디스 채널명, 리스너 설정(매핑)
        return container;
    }

    @Bean
    /**
     <메세지의 흐름>
     * messageService -> redisMessageListener -> listenerAdapter -> onMessage
     : onMessage 실제 메세지 처리 로직 실행 (Stomp 메세지 전송)

     * listenerAdapter : message 랩핑 + onMessage 향해 메세지 전달역할
     */
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean(name = "redisTemplate")
    //Redis 직렬화 방식 GenericJackson2 변경
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory
                                                       ,ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // GenericJackson2JsonRedisSerializer 설정
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Key Serializer 설정
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value Serializer 설정
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet(); // 설정 완료 후 초기화
        return template;
    }
}
