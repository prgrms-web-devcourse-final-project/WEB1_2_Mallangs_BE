package com.mallangs.global.jwt.filter;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import com.mallangs.global.jwt.entity.TokenCategory;
import com.mallangs.global.jwt.service.AccessTokenBlackList;
import com.mallangs.global.jwt.service.RefreshTokenService;
import com.mallangs.global.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final Long accessTokenValidity;
    private final Long accessRefreshTokenValidity;
    private final AccessTokenBlackList accessTokenBlackList;
    private final MemberRepository memberRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // favicon.ico 요청은 필터링하지 않음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return "/favicon.ico".equals(path);
    }

    @Override
    //jwt Token 전용 필터 ( 토큰 유효한지 확인 )
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //uri 확인
            String uri = request.getRequestURI();
            // HTTP 메서드 확인
            String method = request.getMethod();
            //패턴 매처
            AntPathMatcher pathMatcher = new AntPathMatcher();

            Map<String, String> patternVariableMap = new HashMap<>();
            patternVariableMap.put("/api/v1/board/community/category/{categoryId}", "categoryId");
            patternVariableMap.put("/api/v1/comments/board/{boardId}", "boardId");
            patternVariableMap.put("/api/v1/comments/article/{articleId}", "articleId");
            patternVariableMap.put("/api/v1/board/sighting/category/{categoryId}", "categoryId");
            patternVariableMap.put("/api/v1/place-articles/{placeArticleId}/reviews", "placeArticleId");
            patternVariableMap.put("/api/v1/place-articles/{placeArticleId}/reviews/average-score", "placeArticleId");

            // PathVariable 포함 URI 매칭
            for (Map.Entry<String, String> entry : patternVariableMap.entrySet()) {
                String pattern = entry.getKey();
                String variableName = entry.getValue();

                if ("GET".equals(method) && pathMatcher.match(pattern, uri)) {
                    Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(pattern, uri);
                    String variableValue = pathVariables.get(variableName);

                    if (isNumeric(variableValue)) {
                        // 숫자일 경우 필터 체인 진행
                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        // 숫자가 아닐 경우 처리
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 " + variableName + " 입니다.");
                        return;
                    }
                }
            }

            // 단순 경로 매칭 (PathVariable 제외)
            if (("POST".equals(method) && uri.startsWith("/api/v1/member/register")) ||
                    //회원
                    ("POST".equals(method) && uri.startsWith("/api/v1/member/find-user-id")) ||
                    ("POST".equals(method) && uri.startsWith("/api/v1/member/login")) ||
                    ("POST".equals(method) && uri.startsWith("/api/v1/member/find-password")) ||

                    //게시판
                    ("GET".equals(method) && uri.startsWith("/api/v1/board/community")) ||
                    ("GET".equals(method) && uri.startsWith("/api/v1/board/community/keyword")) ||
                    ("GET".equals(method) && uri.startsWith("/api/v1/board/sighting")) ||
                    ("GET".equals(method) && uri.startsWith("/api/v1/board/sighting/keyword")) ||

                    //글타래
                    uri.startsWith("/api/v1/articles/public") ||

                    //반려동물
                    ("GET".equals(method) && uri.startsWith("/api/v1/pets/nearby"))) {
                filterChain.doFilter(request, response);
                return;
            }

            //request 에서 Access Token 꺼내기
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String accessToken = authorizationHeader.substring(7);
                Map<String, Object> claims = jwtUtil.validateToken(accessToken);

                //블랙리스트에 있는지 확인
                if (accessTokenBlackList.checkBlackList(accessToken)) {
                    handleException(response, new Exception("ACCESS TOKEN IS BLOCKED"));
                    return;
                }
                //Access Token 만료 확인
                if (jwtUtil.isExpired(accessToken)) {
                    String refreshTokenFromCookies = getRefreshTokenFromCookies(request);

                    if (refreshTokenFromCookies != null) {
                        try {
                            Map<String, Object> RefreshPayloadMap = jwtUtil.validateRefreshToken(
                                    refreshTokenFromCookies);
                            String refreshTokenInRedis = refreshTokenService.readRefreshTokenInRedis(
                                    RefreshPayloadMap);

                            if (refreshTokenFromCookies.equals(refreshTokenInRedis)) {

                                //userId로 맴버 찾기
                                Member foundMember = memberRepository.findByUserId(
                                                new UserId((String) RefreshPayloadMap.get("userId")))
                                        .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

                                Map<String, Object> payloadMap = new HashMap<>();
                                payloadMap.put("userId", foundMember.getUserId().getValue());
                                payloadMap.put("nickname", foundMember.getNickname().getValue());
                                payloadMap.put("email", foundMember.getEmail().getValue());
                                payloadMap.put("role", foundMember.getMemberRole().name());
                                payloadMap.put("category", TokenCategory.ACCESS_TOKEN.name());

                                //Access Token 새로만들기
                                if (!jwtUtil.isExpired(refreshTokenFromCookies)) {
                                    String newAccessToken = jwtUtil.createAccessToken(payloadMap,
                                            accessTokenValidity);

                                    //Refresh Token 새로 만들기
                                    Map<String, Object> refreshPayloadMap = new HashMap<>();
                                    refreshPayloadMap.put("userId", foundMember.getUserId().getValue());

                                    //식별 위한 UserID 입력
                                    String randomUUID = UUID.randomUUID().toString();
                                    refreshPayloadMap.put("randomUUID", randomUUID);
                                    String newRefreshToken = jwtUtil.createRefreshToken(refreshPayloadMap,
                                            accessRefreshTokenValidity);
                                    refreshTokenService.insertInRedis(refreshPayloadMap, newRefreshToken);

                                    //토큰 전송
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    response.getWriter().write(
                                            "{\"AccessToken\": \"" + newAccessToken + "\"," +
                                                    " \"RefreshToken\": \"" + newRefreshToken + "\",");

                                    response.addHeader("Authorization", "Bearer " + accessToken);
                                    response.addCookie(createCookie(newRefreshToken));
                                    response.setStatus(HttpStatus.OK.value());

                                    //SecurityContextHolder 에 회원 등록
                                    log.info("필터, 리프레시 새로만듬: {}, refresh: {}", newAccessToken, newRefreshToken);
                                    CustomMemberDetails customUserDetails = new CustomMemberDetails(foundMember);
                                    Authentication authToken = new UsernamePasswordAuthenticationToken(
                                            customUserDetails, null, customUserDetails.getAuthorities());
                                    SecurityContextHolder.getContext().setAuthentication(authToken);
                                } else {
                                    handleException(response, new Exception("EXPIRED REFRESH TOKEN"));
                                }
                            } else {
                                handleException(response, new Exception("INVALID REFRESH TOKEN"));
                            }
                        } catch (Exception e) {
                            handleException(response, new Exception("REFRESH TOKEN VALIDATION FAILED"));
                        }
                    } else {
                        handleException(response, new Exception("REFRESH TOKEN NOT FOUND"));
                    }
                    filterChain.doFilter(request, response);
                } else {
                    log.info("Claims: {}", claims);
                    if (claims.get("category") == null || !(claims.get("category")).equals(
                            TokenCategory.ACCESS_TOKEN.name())) {
                        handleException(response, new Exception("INVALID TOKEN CATEGORY"));
                        return;
                    }
                    if (claims.get("userId") == null || claims.get("email") == null
                            || claims.get("role") == null) {
                        handleException(response, new Exception("INVALID TOKEN PAYLOAD"));
                        return;
                    }
                    log.info("맴버아이디 토큰 필터 {}", claims.get("memberId"));
                    Long memberId = ((Integer) claims.get("memberId")).longValue();
                    String userId = claims.get("userId").toString();
                    String nickname = claims.get("nickname").toString();
                    String email = claims.get("email").toString();
                    String role = claims.get("role").toString();
                    Member member = Member.builder()
                            .memberId(memberId)
                            .userId(new UserId(userId))
                            .nickname(new Nickname(nickname))
                            .email(new Email(email))
                            .memberRole(MemberRole.valueOf(role))
                            .build();

                    //검증 통과시 인증정보 SecurityContextHolder 등록
                    log.info("필터, 무난히 통과 member: {}", member.toString());
                    CustomMemberDetails customUserDetails = new CustomMemberDetails(member);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,
                            null, customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("fail to check Tokens: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILED_TO_CHECK_TOKENS);
        }
    }

    //오류시 조치처리
    public void handleException(HttpServletResponse response, Exception e)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
    }

    //쿠키에서 Refresh Token 꺼내기
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

    //쿠키 만들기
    private Cookie createCookie(String refreshCookie) {
        Cookie cookie = new Cookie("RefreshToken", refreshCookie);
        cookie.setMaxAge(3 * 24 * 60 * 60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
    //숫자인지 아닌지 확인하는 코드
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}