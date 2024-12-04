package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Tag(name = "소셜로그인 테스트 API", description = "네이버 소셜로그인 인증 요청주소: `http://localhost:8080/oauth2/authorization/naver`\n" +
        "구글 소셜로그인 인증 요청주소: `http://localhost:8080/oauth2/authorization/google`\n"
+ "인증 완료 후 토큰 생성 API 엔드포인트 = `api/auth/get-access-token`\n"
+ "인증 완료 후 리다이렉트 주소 = `http://localhost:3000/`"
+ "인증 실패 시 리다이렉트 주소 = `/api/member/login`")
public class Oauth2Controller {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    private final MemberRepository memberRepository;

    @GetMapping("/success")
    public String successPage(HttpServletRequest request, Model model) {
        // 세션에서 사용자 정보 추출
        String userId = (String) request.getSession().getAttribute("userId");

        if (userId != null) {
            Optional<Member> memberOpt = memberRepository.findByUserId(new UserId(userId));
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                model.addAttribute("user", member);
            }
        }
        return "success"; // src/main/resources/templates/success.html
    }
}
