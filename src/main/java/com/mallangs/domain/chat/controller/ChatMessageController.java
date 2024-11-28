package com.mallangs.domain.chat.controller;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import com.mallangs.domain.chat.service.ChatMessageService;
import com.mallangs.domain.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/chat")
@Tag(name = "채팅메세지", description = "채팅메세지 CRUD")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final MemberUserService memberUserService;

    //웹소켓 연결 확인 url
    @GetMapping("/websocket-test")
    public String websocketTest(Model model) {
        // 토큰을 동적으로 설정 가능
        String token = "eyJhbGciOiJIUzI1NiIsInR5cGUiOiJKV1QifQ.eyJyb2xlIjoiUk9MRV9VU0VSIiwiY2F0ZWdvcnkiOiJBQ0NFU1NfVE9LRU4iLCJ1c2VySWQiOiJhenNBZDQxMjEiLCJlbWFpbCI6InJrcmt3bmoxMDQ2MjExQGdtYWlsLmNvbSIsIm1lbWJlcklkIjoxLCJpYXQiOjE3MzI4MTY5NDEsImV4cCI6MTczMjgxODc0MX0.SsZmh_lnh98Mbz7uJ8wFfytn0aIsVvHgBVlP4UbV3o0"; // 토큰 예시
        model.addAttribute("token", token);
        return "websocket_test";
    }

    //클라이언트로 부터 오는 메세지 수신 -> Redis로 송신
    @MessageMapping("/send-message")
    public void sendMessage(ChatMessageRequest message) {
        chatMessageService.sendMessage(message);
    }

}
