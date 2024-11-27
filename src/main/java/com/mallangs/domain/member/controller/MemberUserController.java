package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.service.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("api/member")
@Tag(name = "회원", description = "회원 CRUD")
public class MemberUserController {

    private final MemberUserService memberUserService;

    @PostMapping("/register")
    @Operation(summary = "회원등록", description = "회원등록 요청 API")
    public ResponseEntity<String> create(@Validated @RequestBody MemberCreateRequest memberCreateRequest){
        return ResponseEntity.ok(memberUserService.create(memberCreateRequest));
    }

    @GetMapping("")
    @Operation(summary = "회원조회", description = "회원조회 요청 API")
    public ResponseEntity<MemberGetResponse> get(Authentication authentication){
        String userId = authentication.getName();
        return ResponseEntity.ok(memberUserService.get(userId));
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "회원수정", description = "회원수정 요청 API")
    public ResponseEntity<?> update(@Validated @RequestBody MemberUpdateRequest memberUpdateRequest,
                       @PathVariable ("memberId") Long memberId){
        memberUserService.update(memberUpdateRequest,memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴 요청 API")
    public ResponseEntity<?> delete(@PathVariable ("memberId") Long memberId){
        memberUserService.delete(memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @Operation(summary = "회원리스트 조회", description = "회원리스트 조회 요청 API")
    public ResponseEntity<Page<MemberGetResponse>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        return ResponseEntity.ok(memberUserService.getMemberList(pageRequestDTO));
    }

    @PostMapping("/find-user-id")
    @Operation(summary = "아이디찾기", description = "아이디찾기 요청 API")
    public ResponseEntity<String> findUserId(@Validated @RequestBody MemberFindUserIdRequest memberFindUserIdRequest){
        return ResponseEntity.ok(memberUserService.findUserId(memberFindUserIdRequest));
    }

    @PostMapping("/find-password")
    @Operation(summary = "비밀번호찾기", description = "비밀번호찾기 요청 API")
    public ResponseEntity<String> findPassword(@Validated @RequestBody MemberFindPasswordRequest memberFindPasswordRequest) throws MessagingException {
        MemberSendMailResponse mail = memberUserService.findPassword(memberFindPasswordRequest);
        return ResponseEntity.ok(memberUserService.mailSend(mail));
    }
    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@Validated @RequestBody PasswordDTO passwordDTO
                                ,Authentication authentication ){
        String userId = authentication.getName();
        memberUserService.checkPassword(passwordDTO, userId);
        return ResponseEntity.ok().build();
    }
}
