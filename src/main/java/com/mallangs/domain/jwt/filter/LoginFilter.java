package com.mallangs.domain.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.jwt.entity.CustomMemberDetails;
import com.mallangs.domain.jwt.entity.TokenCategory;
import com.mallangs.domain.jwt.service.RefreshTokenService;
import com.mallangs.domain.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Log4j2
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final Long accessTokenValidity;
    private final Long accessRefreshTokenValidity;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    //로그인 정보 인증하기
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String userId = requestMap.get("userId");
            String password = requestMap.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    //로그인 인증 성공시
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomMemberDetails customUserDetails = (CustomMemberDetails) authentication.getPrincipal();

        //customUserDetails에서 인증정보 꺼내기
        String userId = customUserDetails.getUserId();
        String email = customUserDetails.getEmail();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        // Access 토큰 생성
        Map<String, Object> AccessPayloadMap = new HashMap<>();
        AccessPayloadMap.put("userId", userId);
        AccessPayloadMap.put("email", email);
        AccessPayloadMap.put("role", role);
        AccessPayloadMap.put("category", TokenCategory.ACCESS_TOKEN.name());
        String accessToken = jwtUtil.createAccessToken(AccessPayloadMap, accessTokenValidity);
        AccessPayloadMap.put("category", TokenCategory.REFRESH_TOKEN.name());

        //리프레시 토큰 생성 ( 난수를 입력, 의미없는 토큰 생성 )
        Map<String, Object> refreshPayloadMap = new HashMap<>();
        refreshPayloadMap.put("userId", userId);
        //식별 위한 UserID 입력
        String randomUUID = UUID.randomUUID().toString();
        refreshPayloadMap.put("randomUUID", randomUUID);
        String refreshToken = jwtUtil.createRefreshToken(refreshPayloadMap, accessRefreshTokenValidity);
        log.info("로그인, 토큰만듬: {}, refresh: {}", accessToken, refreshToken);

        //리프레시 토큰 레디스에 저장하기
        refreshTokenService.insertInRedis(refreshPayloadMap, refreshToken);

        //토큰 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"AccessToken\": \"" + accessToken + "\"," +
                " \"RefreshToken\": \"" + refreshToken + "\",");

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        //로그인 인증 실패시
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"error\": \"" + failed.getMessage() + "\"}");
        } catch (IOException e) {
            log.error("로그인 인증 실패 :{}", e.getMessage());
        }
    }

    //쿠키 만들기
    private Cookie createCookie(String refreshCookie) {
        Cookie cookie = new Cookie("RefreshToken", refreshCookie);
        cookie.setMaxAge(3*24 * 60 * 60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
