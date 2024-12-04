package com.mallangs.domain.chat.controller;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "WebSocket API", description = "WebSocket 통신을 위한 API 설명")
public class WebSocketDocumentationController {

    @Operation(
            summary = "WebSocket 메세지 전송",
            description = "WebSocket 메시지 (송신)출판 경로`/pub/api/chat/send-message`\n" +
                    "메세지 구독경로 : `/sub/api/chat-room/{chatroomid}`\n Sockjs 경로: `/we-stomp`,",
            requestBody = @RequestBody(
                    description = "채팅 메시지 전송 요청",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "메시지 전송 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            }
    )
    @PostMapping("/pub/api/chat/send-message")
    public void sendMessageExample(@RequestBody ChatMessageRequest message) {
        // 실제 로직은 없고 문서화를 위한 예제만 제공합니다.
    }
}
