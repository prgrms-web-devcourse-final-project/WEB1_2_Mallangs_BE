package com.mallangs.domain.member.jwt.filter;


import com.mallangs.domain.member.jwt.service.AccessTokenBlackList;
import com.mallangs.domain.member.jwt.service.RefreshTokenService;
import com.mallangs.domain.member.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
        /*
            로그아웃에 필터 등록하기
            <비밀번호찾기, 아이디찾기>
        */
@Log4j2
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final AccessTokenBlackList accessTokenBlackList;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    //등록된 필터만 필터링 허용
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equals("/api/member/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Refresh Token 없다면 오류
        String refreshTokenFromCookies = getRefreshTokenFromCookies(request);
        log.info("refreshTokenFromCookies : {}",refreshTokenFromCookies);
        if (refreshTokenFromCookies == null || refreshTokenFromCookies.trim().isEmpty()) {
            log.warn("No refresh token found");
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Refresh Token is missing");
            return;
        }

        // Access Token 없다면 오류
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("No access token found");
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access Token is missing");
            return;
        }

        String accessToken = authorizationHeader.substring(7);
        // 블랙리스트 등록
        try {
            if (!jwtUtil.isExpired(accessToken)) {
                if (!jwtUtil.isExpired(refreshTokenFromCookies)) {
                    accessTokenBlackList.registerBlackList(accessToken, refreshTokenFromCookies);
                    log.info("Tokens are registered to BlackList");
                } else {
                    log.info("RefreshToken is expired");
                }
            } else {
                log.info("AccessToken is expired");

            }
            // 리프레시 토큰 삭제
            Map<String, Object> payloadMap = jwtUtil.validateRefreshToken(refreshTokenFromCookies);
            refreshTokenService.deleteRefreshTokenInRedis(payloadMap);

        } catch (Exception e) {
            log.error("토큰 블랙리스트 처리에 실패하였습니다 : {}", e.getMessage());
            handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token processing failed");
            return;
        }

        // 쿠키 비우기
        Cookie cookie = new Cookie("refreshToken", null);
//        cookie.setSecure(true); // HTTPS 환경에서만 전송
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Logout successful\"}");
        response.getWriter().flush();
    }

    // 리프레시 토큰 꺼내기
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 에러 응답에 등록하기
    private void handleErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
        response.getWriter().flush();
    }
}
