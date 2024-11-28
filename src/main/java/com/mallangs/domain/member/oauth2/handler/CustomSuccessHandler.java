package com.mallangs.domain.member.oauth2.handler;

import com.mallangs.domain.member.jwt.entity.TokenCategory;
import com.mallangs.domain.member.jwt.service.RefreshTokenService;
import com.mallangs.domain.member.jwt.util.JWTUtil;
import com.mallangs.domain.member.oauth2.dto.CustomOAuth2Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    /**
     CustomOAuth2MemberService OAuth2인증 성공 시
     토큰 발행 객체
     */
    @Value("${spring.jwt.access-token-validity-in-minutes}") private Long accessTokenValidity;
    @Value("${spring.jwt.refresh-token-validity-in-minutes}") private Long accessRefreshTokenValidity;

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        //OAuth2User 에서 사용자 정보 추출
        CustomOAuth2Member customUserDetails = (CustomOAuth2Member) authentication.getPrincipal();

        //userId, email 추출
        String userId = customUserDetails.getName();
        String email = customUserDetails.getEmail();

        //권한 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //Access 토큰 생성
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("userId", userId);
        payloadMap.put("email", email);
        payloadMap.put("role", role);
        payloadMap.put("category", TokenCategory.ACCESS_TOKEN.name());
        String accessToken = jwtUtil.createAccessToken(payloadMap, accessTokenValidity);

        //Refresh 토큰 생성
        payloadMap = new HashMap<>();
        payloadMap.put("userId", userId);
        // 식별 위한 UserID 입력
        String randomUUID = UUID.randomUUID().toString();
        payloadMap.put("randomUUID", randomUUID);
        String refreshToken = jwtUtil.createRefreshToken(payloadMap, accessRefreshTokenValidity);
        log.info("OAuth2로그인, 토큰만듬: {}, refresh: {}", accessToken,refreshToken);

        //Refresh redis에 저장
        refreshTokenService.insertInRedis(payloadMap, refreshToken);

        //토큰 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"AccessToken\": \"" + accessToken + "\"," +
                        " \"RefreshToken\": \"" + refreshToken + "\",");

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());

        // 로그인 후 리다이렉트 -> 어떤 페이지로 이동할 서버에서 선택!
        response.sendRedirect("http://localhost:3000/");
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
