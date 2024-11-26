package com.mallangs.domain.oauth2.handler;

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
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //배포 환경시에는 아래 메세지로 바꿔주기 (자세한 내용 클라로 전송 X)
        String errorMessage = "Authentication failed. Please check your credentials or try again.";
        try {
            response.getWriter().write("{\"error\": \"" + exception.getMessage() + "\"}");
        } catch (IOException e) {
            log.error("로그인 인증 실패 :{}", e.getMessage());
        }
    }
}
