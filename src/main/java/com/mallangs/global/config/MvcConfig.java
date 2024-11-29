package com.mallangs.global.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000", "https://*.ngrok-free.app")  // 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 허용할 헤더
                .allowCredentials(true);  // 인증 정보 허용 여부
    }
}