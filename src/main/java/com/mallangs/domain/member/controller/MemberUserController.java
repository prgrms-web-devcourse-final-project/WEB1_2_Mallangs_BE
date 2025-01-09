package com.mallangs.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.dto.request.LoginRequest;
import com.mallangs.domain.member.dto.response.MemberGetByOtherResponse;
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
import org.springframework.data.redis.core.RedisTemplate;
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
import java.net.http.HttpResponse;
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
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 토큰 만료 시간
    @Value("${spring.jwt.access-token-validity-in-minutes}")
    private Long accessTokenValidity;

    private final AuthenticationManager authenticationManager;
    private final MemberUserService memberUserService;
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

    @GetMapping("/member-id/{memberId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "회원 프로필 조회", description = "회원 프로필 조회 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "406", description = "차단된 계정입니다.")
    })
    public ResponseEntity<MemberGetResponse> get(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.get(memberId));
    }
    @GetMapping("/other/member-id/{memberId}")
    @Operation(summary = "(타인)회원 프로필 조회", description = "(타인)회원 프로필 조회 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "406", description = "차단된 계정입니다.")
    })
    public ResponseEntity<MemberGetByOtherResponse> getByOther(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.getByOther(memberId));
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
    @GetMapping("/other/{userId}")
    @Operation(summary = "(타인)회원 프로필 조회", description = "(타인)회원 프로필 조회 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "406", description = "차단된 계정입니다.")
    })
    public ResponseEntity<MemberGetByOtherResponse> getByOther(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberUserService.getByOther(userId));
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
    public ResponseEntity<?> login(HttpServletResponse response, HttpServletRequest request,
                                   @Validated @RequestBody LoginRequest loginRequest) throws MessagingException {
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

            //로그인 시간 저장
            Member foundMember = memberRepository.findByUserId(new UserId(userId))
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
            foundMember.recordLoginTime();
            memberRepository.save(foundMember);

            //차단계정인지 확인
            if (!foundMember.getIsActive()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(foundMember.getNickname().getValue() + "님은 " + foundMember.getReasonForBan() + " 이유로 "
                                + (foundMember.getExpiryDate().getDayOfYear() - LocalDateTime.now().getDayOfYear()) + "일간 웹서비스 이용 제한됩니다.");
            }

            //ip주소 확인/저장
            Object ipAddress = redisTemplate.opsForValue().get(userId);
            if (ipAddress==null){
                //ip주소 저장
                ipAddress = request.getRemoteAddr();
                redisTemplate.opsForValue().set(userId,ipAddress);
            }else {
                if (!(ipAddress).equals(request.getRemoteAddr())){
                    //경고메세지 전송
                    memberUserService.mailSend(memberUserService.writeWarningMessage(email));
                };
            }

            //토큰 전송
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.addHeader("Authorization", "Bearer " + accessToken);
            response.setStatus(HttpStatus.OK.value());

            // 응답 반환
            return ResponseEntity.ok(Map.of(
                    "AccessToken", accessToken
//                    "RefreshToken", refreshToken
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
        response.setHeader("accessToken", null);
        return ResponseEntity.status(HttpStatus.CREATED).body("message : 로그아웃 성공");
    }

    //ip주소 변경
    @GetMapping("/change/ip-address")
    public String changeIpAddress(@RequestParam String userId, HttpServletRequest request) {
        redisTemplate.opsForValue().set(userId, request.getRemoteAddr());
        log.info("변경된 ip주소 : {}",request.getRemoteAddr());
        return "ip주소가 변경되었습니다.";
    }

}
