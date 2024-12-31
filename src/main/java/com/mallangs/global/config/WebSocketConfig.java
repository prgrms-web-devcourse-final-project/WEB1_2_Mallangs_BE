package com.mallangs.global.config;

import com.mallangs.global.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@Log4j2
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private TaskScheduler messageBrokerTaskScheduler;

    @Autowired
    public void setMessageTaskScheduler(@Lazy TaskScheduler taskScheduler) {
        this.messageBrokerTaskScheduler = taskScheduler;
    }

    //stomp로 pub/sub 주소 생성
    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub")
                //heart beat 주기 (클라이언트_전송_간격, 서버_수신_간격) -> 서버에서 먼저 보내고, 클라가 응답하는 구조 0.1초, 0.2초
                .setHeartbeatValue(new long[] {100, 200})
                .setTaskScheduler(this.messageBrokerTaskScheduler);
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Bean
    //글자,이미지 파일 제한 500바이트 까지(오류 방지위함)
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean(); // (3)
        container.setMaxTextMessageBufferSize(500000); // (4)
        container.setMaxBinaryMessageBufferSize(500000); // (5)
        return container;
    }

    //endPoint 설정
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    //메세지 가로채는 핸들러
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        MappingJackson2MessageConverter jackson2MessageConverter = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setStrictContentTypeMatch(false);
        return jackson2MessageConverter;
    }

    @EventListener
    public void connectEvent(SessionConnectEvent sessionConnectEvent){
        log.info(sessionConnectEvent);
        log.info("웹소켓 연결 성공");
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info(sessionDisconnectEvent.getSessionId());
        log.info("웹소켓 연결 끊어짐");
    }
}
