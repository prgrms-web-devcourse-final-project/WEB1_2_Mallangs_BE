package com.mallangs.global.config;

import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.jwt.filter.JWTFilter;
import com.mallangs.global.jwt.service.AccessTokenBlackList;
import com.mallangs.global.jwt.service.RefreshTokenService;
import com.mallangs.global.jwt.util.JWTUtil;
import com.mallangs.global.oauth2.handler.CustomFailureHandler;
import com.mallangs.global.oauth2.handler.CustomSuccessHandler;
import com.mallangs.global.oauth2.service.CustomOAuth2MemberService;

import java.util.Arrays;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor

public class SecurityConfig {

    // 토큰 만료 시간
    @Value("${spring.jwt.access-token-validity-in-minutes}")
    private Long accessTokenValidity;
    @Value("${spring.jwt.refresh-token-validity-in-minutes}")
    private Long accessRefreshTokenValidity;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenBlackList accessTokenBlackList;
    private final MemberRepository memberRepository;
    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // cors 필터
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(
                            Arrays.asList("http://localhost:3000", "http://localhost:8080", "https://mallang-place.vercel.app"
                                    ,"https://mallangplace.site","http://localhost:5173", "https://*.ngrok-free.app"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;

                }));

        // 다른 기능 정지
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

//        // oauth2
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .loginPage("/login")
//                        .userInfoEndpoint((userInfo) -> userInfo
//                                .userService(customOAuth2MemberService))
//                        .successHandler(customSuccessHandler)
//                        .failureHandler(customFailureHandler)
//                );

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) ->
                        auth
                                .requestMatchers("/favicon.ico").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/api/v1/member/register", "/api/v1/member/login",
                                        "/api/v1/member/logout", "/api/v1/member/find-user-id",
                                        "/api/v1/member/find-password").permitAll() //회원가입,로그인,로그아웃,비번찾기,아이디찾기
//                                .requestMatchers("/api/v1/member/oauth2/**").permitAll() //소셜로그인
                                .requestMatchers("/api/v1/member/admin/**").hasRole("ADMIN") //관리자
                                .requestMatchers("/api/v1/member/**").permitAll() //회원
                                .requestMatchers("/api/v1/address/**").permitAll() //주소
                                .requestMatchers("/api/v1/pets/**").permitAll() //반려동물
                                .requestMatchers("/api/v1/image/**").permitAll() //이미지
                                .requestMatchers("/api/v1/chat-room/**").permitAll() //채팅방
                                .requestMatchers("/api/v1/chat/**").permitAll() //채팅
                                .requestMatchers("/api/chat/**").permitAll() //채팅
                                .requestMatchers("/api/v1/board/**").permitAll() //게시판
                                .requestMatchers("/api/v1/category/**").permitAll() //카테고리
                                .requestMatchers("/api/v1/articles/public/**").permitAll() //글타래
                                .requestMatchers("/api/v1/place-articles/**").permitAll() //장소
                                .requestMatchers("/api/v1/comments/**").permitAll() //댓글
                                .requestMatchers("/tourapi/v1/**").permitAll() //공공데이터
                                .requestMatchers("/api/v1/token").permitAll() //토큰
                                .requestMatchers("/webjars/**").permitAll() //웹소켓
                                .requestMatchers("/ws-stomp/**").permitAll() //웹소켓
                                .requestMatchers("/chat-rooms/**").permitAll() // 웹소켓
                                .requestMatchers("/api/chat/websocket-test").permitAll() //웹소켓
                                .requestMatchers("/api/v1/ai/**").permitAll() //AI

                                // Swagger UI 관련 경로 허용
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()

                                //헬스체크
                                .requestMatchers("/actuator/health").permitAll()
                                .anyRequest().authenticated());
        // 필터
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTFilter(jwtUtil, refreshTokenService, accessTokenValidity
                                , accessRefreshTokenValidity, accessTokenBlackList, memberRepository)
                        , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

