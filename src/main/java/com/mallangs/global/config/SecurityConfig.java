package com.mallangs.global.config;

import com.mallangs.domain.member.jwt.filter.JWTFilter;
import com.mallangs.domain.member.jwt.filter.LoginFilter;
import com.mallangs.domain.member.jwt.filter.LogoutFilter;
import com.mallangs.domain.member.jwt.service.AccessTokenBlackList;
import com.mallangs.domain.member.jwt.service.RefreshTokenService;
import com.mallangs.domain.member.jwt.util.JWTUtil;
import com.mallangs.domain.member.oauth2.handler.CustomFailureHandler;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.oauth2.handler.CustomSuccessHandler;
import com.mallangs.domain.member.oauth2.service.CustomOAuth2MemberService;
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

import java.util.Arrays;
import java.util.Collections;

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // cors 필터
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;

                }));

        // 서버 접근 설정
        AuthenticationManager authManager = authenticationManager(authenticationConfiguration);

        // 로그인 필터 생성 및 설정
        LoginFilter loginFilter = new LoginFilter(accessTokenValidity, accessRefreshTokenValidity,
                                        authManager, jwtUtil, refreshTokenService,memberRepository);
        loginFilter.setFilterProcessesUrl("/api/member/login");

        //로그아웃 필터 생성
        LogoutFilter logoutFilter = new LogoutFilter(accessTokenBlackList, jwtUtil, refreshTokenService);

        // 다른 기능 정지
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo
                                .userService(customOAuth2MemberService))
                        .successHandler(customSuccessHandler)
                        .failureHandler(customFailureHandler)
                );

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) ->
                        auth
                        .requestMatchers("/api/member/register", "/api/member/login",
                                "/api/member/logout","/api/member/find-user-id",
                                "/api/member/find-password").permitAll()
                        .requestMatchers("/api/member/oauth2/**").permitAll()
                        .requestMatchers("/api/member/admin").hasRole("ADMIN")
                        .requestMatchers("/api/member/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/address/**").permitAll()
                        .requestMatchers("/api/member-file-test").permitAll()
                        // Swagger UI 관련 경로 허용
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .anyRequest().authenticated());
        // 필터
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, refreshTokenService, accessTokenValidity, accessRefreshTokenValidity, accessTokenBlackList, memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(logoutFilter, JWTFilter.class);
        return http.build();
    }
}

