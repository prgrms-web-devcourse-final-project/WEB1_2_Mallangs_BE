package com.mallangs.domain.chat.controller;

import com.mallangs.domain.chat.dto.request.ChatMessageRequest;
import com.mallangs.domain.chat.dto.request.UpdateChatMessageRequest;
import com.mallangs.domain.chat.dto.response.ChatMessageListResponse;
import com.mallangs.domain.chat.dto.response.ChatMessageResponse;
import com.mallangs.domain.chat.dto.response.ChatMessageToDTOResponse;
import com.mallangs.domain.chat.service.ChatMessageService;
import com.mallangs.domain.member.dto.PageRequestDTO;
import com.mallangs.domain.member.service.MemberUserService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    //채팅메세지 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "채팅메세지 조회", description = "채팅 이력을 불러오는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "채팅방이 존재하지 않습니다.")
    })
    public ResponseEntity<Page<ChatMessageListResponse>> get(@RequestParam("chatRoomId") Long chatRoomId,
                                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        return ResponseEntity.ok(chatMessageService.getPage(pageRequestDTO, chatRoomId));
    }

    //채팅메세지 수정
    @PutMapping
    @ResponseBody
    @Operation(summary = "채팅메세지 수정", description = "채팅내용을 수정하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "채팅메세지가 존재하지 않습니다.")
    })
    public ResponseEntity<ChatMessageToDTOResponse> update(
            @Validated @RequestBody UpdateChatMessageRequest updateChatMessageRequest) {
        return ResponseEntity.ok(chatMessageService.update(updateChatMessageRequest));
    }

    //채팅메세지 삭제
    @DeleteMapping("/{chatMessageId}")
    @ResponseBody
    @Operation(summary = "채팅메세지 삭제", description = "채팅내용을 삭제하는 API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "채팅메세지가 존재하지 않습니다.")
    })
    public ResponseEntity<?> delete(@PathVariable("chatMessageId") Long chatMessageId) {
        if (chatMessageService.delete(chatMessageId)){
            return ResponseEntity.ok("삭제성공");
        };
        return ResponseEntity.noContent().build();
    }

    //채팅 메세지 읽음으로 변경
    @ResponseBody
    @PutMapping("/{chatMessageId}/{participatedRoomId}")
    @Operation(summary = "채팅메세지 읽음으로 변경", description = "채팅메세지를 읽음으로 변경하는 API.")
    public ResponseEntity<?> changeStatus(@PathVariable Long chatMessageId,
                             @PathVariable Long participatedRoomId,
                             @AuthenticationPrincipal CustomMemberDetails customMemberDetails){
        String nickname = customMemberDetails.getNickname();
        chatMessageService.changeUnReadToRead(chatMessageId, participatedRoomId, nickname);
        return ResponseEntity.ok("변경 성공");
    }
}
