package com.mallangs.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
//@Profile("local") // 개발 환경에서만 활성화
public class RedirectHttpsToHttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // HTTPS 요청을 HTTP로 리디렉션
        if ("https".equals(httpRequest.getScheme())) {
            String httpUrl = "http://" + httpRequest.getServerName() + httpRequest.getRequestURI();
            httpResponse.sendRedirect(httpUrl);
        } else {
            chain.doFilter(request, response);
        }
    }
}