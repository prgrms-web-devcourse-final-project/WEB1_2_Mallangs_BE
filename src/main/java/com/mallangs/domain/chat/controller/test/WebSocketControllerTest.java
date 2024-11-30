package com.mallangs.domain.chat.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Log4j2
@Tag(name = "테스트용 컨트롤러 사용 X", description = " 테스트용 사용 X")
public class WebSocketControllerTest {

    //웹소켓 연결 확인 url
    @GetMapping("api/chat/websocket-test")
    @Operation(summary = "테스트 사용X", description = "테스트용 컨트롤러 사용X")
    public String websocketTest(Model model) {
        // 토큰을 동적으로 설정 가능
        String token = "put Token here"; // 토큰 예시
        model.addAttribute("token", token);
        return "websocket_test";
    }


}
