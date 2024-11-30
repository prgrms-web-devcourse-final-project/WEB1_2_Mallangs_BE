package com.mallangs.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Log4j2
@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    /**
     CustomOAuth2MemberService OAuth2인증 실패 시
             오류 메세지 보내기
     */

    public CustomFailureHandler() {
        // 로그인 실패 시 이동할 URL 설정
        setDefaultFailureUrl("/api/member/login");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorMessage = "소셜로그인 인증에 실패했습니다. 로그인 정보를 확인해주세요.";
        try {
            response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
        } catch (IOException e) {
            log.error("Oauth2 로그인 인증 실패 :{}", errorMessage);
        }finally {
            request.getSession().setAttribute("errorMessage", "소셜로그인 인증에 실패했습니다. 로그인 정보를 확인해주세요.");
            super.onAuthenticationFailure(request, response, exception);
        }
    }

}
