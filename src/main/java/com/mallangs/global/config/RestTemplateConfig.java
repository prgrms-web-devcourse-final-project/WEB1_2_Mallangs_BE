package com.mallangs.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration // 설정 클래스 지정
public class RestTemplateConfig {

    @Value("${server.connection-timeout.second}") // application.properties에서 연결 시간 제한 값 주입
    private int connectionTimeout;

    @Value("${server.response-timeout.second}") // application.properties에서 응답 시간 제한 값 주입
    private int responseTimeout;

    /**
     * RestTemplate 빈 생성
     * @param restTemplateBuilder RestTemplateBuilder
     * @return RestTemplate 객체
     */
    @Bean // 빈으로 등록하여 의존성 주입 가능하게 함
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout)) // 연결 시간 제한 설정
                .setReadTimeout(Duration.ofSeconds(responseTimeout)) // 응답 시간 제한 설정
                .build(); // RestTemplate 객체 생성
    }


}