package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.dto.request.LoginRequest;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.service.MemberUserService;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import com.mallangs.global.jwt.entity.TokenCategory;
import com.mallangs.global.jwt.filter.LoginFilter;
import com.mallangs.global.jwt.service.AccessTokenBlackList;
import com.mallangs.global.jwt.service.RefreshTokenService;
import com.mallangs.global.jwt.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
@Tag(name = "회원", description = "회원 CRUD")
public class MemberUserController {

    private final MemberRepository memberRepository;
    // 토큰 만료 시간
    @Value("${spring.jwt.access-token-validity-in-minutes}")
    private Long accessTokenValidity;
    @Value("${spring.jwt.refresh-token-validity-in-minutes}")
    private Long accessRefreshTokenValidity;

    private final AuthenticationManager authenticationManager;
    private final MemberUserService memberUserService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenBlackList accessTokenBlackList;
    private final JWTUtil jwtUtil;


    @PostMapping("/register")
    @Operation(summary = "회원등록", description = "회원등록 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 등록 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버에 문제가 발생했습니다.")
    })
    public ResponseEntity<MemberRegisterRequest> create(@Validated @RequestBody MemberCreateRequest memberCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.create(memberCreateRequest));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "회원 프로필 조회", description = "회원 프로필 조회 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "406", description = "차단된 계정입니다.")
    })
    public ResponseEntity<MemberGetResponse> get(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.get(userId));
    }

    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "회원수정", description = "회원수정 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버에 문제가 발생했습니다.")
    })
    public ResponseEntity<MemberGetResponse> update(@Validated @RequestBody MemberUpdateRequest memberUpdateRequest,
                                                    @PathVariable("memberId") Long memberId) {

        return ResponseEntity.status(HttpStatus.CREATED).
                body(memberUserService.update(memberUpdateRequest, memberId));
    }

    @PostMapping("/find-user-id")
    @Operation(summary = "아이디찾기", description = "아이디찾기 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "아이디 찾기 요청 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
    })
    public ResponseEntity<String> findUserId(@Validated @RequestBody MemberFindUserIdRequest memberFindUserIdRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberUserService.findUserId(memberFindUserIdRequest));
    }

    @PostMapping("/find-password")
    @Operation(summary = "비밀번호찾기", description = "비밀번호찾기 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 찾기 요청 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
    })
    public ResponseEntity<MemberSendMailResponse> findPassword(@Validated @RequestBody MemberFindPasswordRequest memberFindPasswordRequest) throws MessagingException {
        MemberSendMailResponse mail = memberUserService.findPassword(memberFindPasswordRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.mailSend(mail));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/check-password")
    @Operation(summary = "비밀번호 확인", description = "비밀번호 확인 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 확인요청 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다.")
    })
    public ResponseEntity<MemberCheckPasswordResponse> checkPassword(@Validated @RequestBody PasswordDTO passwordDTO
            , Authentication authentication) {
        String userId = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.checkPassword(passwordDTO, userId));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "로그인 요청 성공"),
            @ApiResponse(responseCode = "401", description = "로그인에 실패했습니다.")
    })
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());

            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(authToken);

            ///customUserDetails에서 인증정보 꺼내기
            CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
            Long memberId = customMemberDetails.getMemberId();
            String userId = customMemberDetails.getUsername();
            String nickname = customMemberDetails.getNickname();
            String email = customMemberDetails.getEmail();
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            // Access 토큰 생성
            Map<String, Object> AccessPayloadMap = new HashMap<>();
            AccessPayloadMap.put("memberId", memberId);
            AccessPayloadMap.put("userId", userId);
            AccessPayloadMap.put("nickname", nickname);
            AccessPayloadMap.put("email", email);
            AccessPayloadMap.put("role", role);
            AccessPayloadMap.put("category", TokenCategory.ACCESS_TOKEN.name());
            String accessToken = jwtUtil.createAccessToken(AccessPayloadMap, accessTokenValidity);

            //리프레시 토큰 생성 ( 난수를 입력, 의미없는 토큰 생성 )
            Map<String, Object> refreshPayloadMap = new HashMap<>();
            refreshPayloadMap.put("userId", userId);

            //식별 위한 UserID 입력
            String randomUUID = UUID.randomUUID().toString();
            refreshPayloadMap.put("randomUUID", randomUUID);
            String refreshToken = jwtUtil.createRefreshToken(refreshPayloadMap, accessRefreshTokenValidity);
            log.info("컨트롤러 로그인, 토큰만듬: {}, refresh: {}", accessToken, refreshToken);

            //리프레시 토큰 레디스에 저장하기
            refreshTokenService.insertInRedis(refreshPayloadMap, refreshToken);

            //로그인 시간 저장
            Member foundMember = memberRepository.findByUserId(new UserId(userId))
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
            foundMember.recordLoginTime();
            memberRepository.save(foundMember);

            //차단계정인지 확인
            if (!foundMember.getIsActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(foundMember.getNickname().getValue() + "님은 " + foundMember.getReasonForBan() + " 이유로 "
                                + (foundMember.getExpiryDate().getDayOfYear() - LocalDateTime.now().getDayOfYear()) + "일간 웹서비스 이용 제한됩니다.");
            }

            // 응답 반환
            return ResponseEntity.ok(Map.of(
                    "AccessToken", accessToken,
                    "RefreshToken", refreshToken
            ));
        } catch (AuthenticationException ex) {
            // 인증 실패 시 401 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다."));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "로그아웃 요청 성공"),
            @ApiResponse(responseCode = "401", description = "토큰이 없습니다.")
    })
    public ResponseEntity<?> loginOut(HttpServletRequest request, HttpServletResponse response) {
        log.info("커스텀 로그아웃 실행");

        // Refresh Token 없다면 오류
        String refreshTokenFromCookies = getRefreshTokenFromCookies(request);
        log.info("refreshTokenFromCookies : {}", refreshTokenFromCookies);
        if (refreshTokenFromCookies == null || refreshTokenFromCookies.trim().isEmpty()) {
            log.warn("No refresh token found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token is missing"));
        }

        // Access Token 없다면 오류
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("No access token found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Access token is missing"));
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
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Token processing failed"));
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

        return ResponseEntity.status(HttpStatus.CREATED).body("LOGOUT SUCCESSFUL");
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


    @PutMapping("/member-role")
    @Operation(summary = "관리자로 권한변환", description = "관리자로 권한 변환 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "관리자로 권한 병환 성공"),
            @ApiResponse(responseCode = "400", description = "관리자로 권한 병환 실패.")
    })
    public ResponseEntity<?> changeMemberRole(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        String userId = customMemberDetails.getUserId();
        //맴버 찾기
        Member foundMember = memberRepository.findByUserId(new UserId(userId))
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        //권한 변경
        foundMember.changeRole(MemberRole.ROLE_ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberRepository.save(foundMember));
    }

}
