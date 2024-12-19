package com.mallangs.domain.member.controller;

import com.mallangs.global.jwt.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "소셜로그인 / 세션으로 토큰 받는 컨트롤러", description = "네이버 소셜로그인 인증 요청주소: `http://localhost:8080/oauth2/authorization/naver`\n" +
        "`구글 소셜로그인 인증 요청주소: http://localhost:8080/oauth2/authorization/google`\n"
        + "인증 완료 후 토큰 생성 API 엔드포인트 = `api/auth/get-access-token`\n"
        + "인증 완료 후 리다이렉트 주소 = `http://localhost:3000/`"
        + "인증 실패 시 리다이렉트 주소 = `/api/member/login`")
public class AuthController {

    private final JWTUtil jwtUtil;

    /**
     * AccessToken을 클라이언트로 제공하는 API
     */
    @GetMapping("/get-access-token")
    @Operation(summary = "세션으로 토큰 받아가는 API", description = "AccessToken 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전달 성공"),
            @ApiResponse(responseCode = "404", description = "토큰 전달 실패.")
    })
    public ResponseEntity<?> getAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // AccessToken을 세션에서 가져오기
        String accessToken = (String) request.getSession().getAttribute("AccessToken");

        if (accessToken == null || accessToken.trim().isEmpty()) {
            log.warn("No AccessToken found in session");
            return ResponseEntity.status(401).body(Map.of("error", "Access Token is missing"));
        }

        // AccessToken을 클라이언트로 전달
        return ResponseEntity.ok(Map.of("AccessToken", accessToken));
    }
}