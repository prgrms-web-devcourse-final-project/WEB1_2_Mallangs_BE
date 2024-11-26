package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.MemberGetResponse;
import com.mallangs.domain.member.dto.PageRequestDTO;
import com.mallangs.domain.member.service.MemberAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member/admin")
@Tag(name = "관리자", description = "관리자 CRUD")
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원영구 삭제", description = "회원영구 삭제 요청 API")
    public void delete(@PathVariable("memberId") Long memberId) {
        memberAdminService.delete(memberId);
    }

    @GetMapping("/user-id")
    @Operation(summary = "유저 아이디로 회원검색", description = "유저 아이디로 회원검색 요청 API")
    public ResponseEntity<List<MemberGetResponse>> searchByUserId(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(memberAdminService.getMemberByUserId(userId));
    }

    @GetMapping("/email")
    @Operation(summary = "유저 이메일로 회원검색", description = "유저 이메일 회원검색 요청 API")
    public ResponseEntity<List<MemberGetResponse>> searchByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(memberAdminService.getMemberByEmail(email));
    }

    @GetMapping("/nickname")
    @Operation(summary = "유저 닉네임으로 회원검색", description = "유저 닉네임으로 회원검색 요청 API")
    public ResponseEntity<List<MemberGetResponse>> searchByNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(memberAdminService.getMemberByNickname(nickname));
    }

    @GetMapping("/list")
    @Operation(summary = "회원리스트 조회", description = "회원리스트 조회 요청 API")
    public ResponseEntity<Page<MemberGetResponse>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        return ResponseEntity.ok(memberAdminService.getMemberList(pageRequestDTO));
    }

}
