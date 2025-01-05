//package com.mallangs.global.jwt.controller;
//
//import com.mallangs.global.jwt.dto.TokensRequest;
//import com.mallangs.global.jwt.dto.TokensResponse;
//
//import com.mallangs.domain.member.entity.Member;
//import com.mallangs.domain.member.entity.embadded.UserId;
//import com.mallangs.domain.member.repository.MemberRepository;
//import com.mallangs.global.exception.ErrorCode;
//import com.mallangs.global.exception.MallangsCustomException;
//import com.mallangs.global.jwt.entity.TokenCategory;
//import com.mallangs.global.jwt.service.RefreshTokenService;
//import com.mallangs.global.jwt.util.JWTUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//@Log4j2
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/v1/token")
//public class TokenController {
//
//    @Value("${spring.jwt.access-token-validity-in-minutes}")
//    private Long accessTokenValidity;
//
//    private final JWTUtil jwtUtil;
//    private final RefreshTokenService refreshTokenService;
//    private final MemberRepository memberRepository;
//
//    @PostMapping
//    @Operation(summary = "토큰 받아가는 API- Access 토큰은 재발행되고, Refresh 토큰은 확인 후 전달", description = "토큰 요청 API")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "전달 성공"),
//            @ApiResponse(responseCode = "400", description = "토큰 전달 실패.")
//    })
//    public ResponseEntity<TokensResponse> getTokens(@RequestBody TokensRequest tokensRequest){
//
//        log.info("refreshToken {}", tokensRequest.getRefreshToken());
//
//        if (jwtUtil.isExpired(tokensRequest.getRefreshToken())) {
//            throw new MallangsCustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
//        }
//        Map<String, Object> payload = jwtUtil.validateRefreshToken(tokensRequest.getRefreshToken());
//        String refreshToken = refreshTokenService.readRefreshTokenInRedis(payload);
//        if (refreshToken == null) {
//            throw new MallangsCustomException(ErrorCode.REFRESH_TOKEN_MISSING);
//        }
//
//        //userId로 맴버 찾기
//        Member foundMember = memberRepository.findByUserId(
//                        new UserId((String) payload.get("userId")))
//                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        Map<String, Object> payloadMap = new HashMap<>();
//        payloadMap.put("userId", foundMember.getUserId().getValue());
//        payloadMap.put("nickname", foundMember.getNickname().getValue());
//        payloadMap.put("email", foundMember.getEmail().getValue());
//        payloadMap.put("role", foundMember.getMemberRole().name());
//        payloadMap.put("category", TokenCategory.ACCESS_TOKEN.name());
//
//        //Access Token 새로만들기
//        String newAccessToken = jwtUtil.createAccessToken(payloadMap, accessTokenValidity);
//
//        TokensResponse tokens = TokensResponse.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
//
//        return ResponseEntity.ok(tokens);
//    }
//
//}
