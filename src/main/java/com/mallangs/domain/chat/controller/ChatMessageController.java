package com.mallangs.domain.chat.controller;

import com.mallangs.domain.chat.dto.ChatMessageRequest;
import com.mallangs.domain.chat.service.ChatMessageService;
import com.mallangs.domain.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/chat")
@Tag(name = "채팅메세지", description = "채팅메세지 CRUD")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final MemberUserService memberUserService;

    //클라이언트로 부터 오는 메세지 수신 -> Redis로 송신
    @MessageMapping("/api/chat/send-message")
    public void sendMessage(ChatMessageRequest message) {
        chatMessageService.sendMessage(message);
    }
}
