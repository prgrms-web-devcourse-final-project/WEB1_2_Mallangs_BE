package com.mallangs.domain.chat.controller;

import com.mallangs.domain.chat.dto.response.ParticipatedRoomListResponse;
import com.mallangs.domain.chat.service.ChatMessageService;
import com.mallangs.domain.chat.service.ChatRoomService;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/chat-room")
@Tag(name = "채팅방", description = "채팅방 CRUD")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    //채팅방 생성
    @PostMapping("/{memberId}")
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
    })
    public ResponseEntity<Long> create(@PathVariable("memberId") Long memberId,
                                       @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

        Long myId = customMemberDetails.getMemberId();
        Long roomId = chatRoomService.create(memberId, myId);
        return ResponseEntity.created(URI.create("/api/chat-room/" + roomId)).body(roomId);
    }

    //채팅방리스트 조회
    @GetMapping
    @Operation(summary = "채팅방 조회", description = "나와 관련된 채팅방을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "채팅방이 존재하지 않습니다.")
    })
    public ResponseEntity<List<ParticipatedRoomListResponse>> getList(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        log.info("채팅방 조회 memberId 입력값: {}", customMemberDetails.getMemberId());
        return ResponseEntity.ok(chatRoomService.get(customMemberDetails.getMemberId()));
    }

//    //채팅방 수정
//    @PutMapping
//    @Operation(summary = "채팅방 수정", description = "채팅방 이름을 수정합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "수정 성공"),
//            @ApiResponse(responseCode = "404", description = "채팅방이 존재하지 않습니다.")
//    })
//    public ResponseEntity<?> update(@Validated @RequestBody ChatRoomChangeNameRequest chatRoomChangeNameRequest) {
//        chatRoomService.update(chatRoomChangeNameRequest);
//        return ResponseEntity.ok().build();
//    }

    //채팅방 삭제
    @DeleteMapping("/{participatedRoomId}")
    @Operation(summary = "참여 채팅방 삭제", description = "참여 채팅방을 나갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "참여 채팅방이 존재하지 않습니다.")
    })
    public ResponseEntity<?> delete(@PathVariable("participatedRoomId") Long participatedRoomId,
                                    Authentication authentication) {
        chatRoomService.delete(authentication.getName(), participatedRoomId);
        return ResponseEntity.ok().build();
    }


    //채팅방 조회
    @ResponseBody
    @GetMapping("/{participatedRoomId}")
    @Operation(summary = "채팅메세지 읽음으로 변경", description = "채팅메세지를 읽음으로 변경하는 API.")
    public ResponseEntity<?> changeStatus(@PathVariable Long participatedRoomId,
                                          @AuthenticationPrincipal CustomMemberDetails customMemberDetails){

        String nickname = customMemberDetails.getNickname();

        chatMessageService.changeUnReadToRead(participatedRoomId, nickname);
        return ResponseEntity.ok("변경 성공");
    }

}
