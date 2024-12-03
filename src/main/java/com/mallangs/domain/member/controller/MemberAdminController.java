package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.service.MemberAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member/admin")
@Tag(name = "관리자", description = "관리자 CRUD")
public class MemberAdminController {

    private final MemberAdminService memberAdminService;

    @PostMapping("/ban")
    @Operation(summary = "회원차단", description = "회원차단 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 차단 성공"),
            @ApiResponse(responseCode = "404", description = "회원 차단 실패.")
    })
    public ResponseEntity<Integer> delete(@Validated @RequestBody MemberBanRequest memberBanRequest) {
        return ResponseEntity.ok(memberAdminService.banMember(memberBanRequest));
    }

    @PostMapping("/user-id")
    @Operation(summary = "유저아이디로 회원리스트 검색", description = "유저아이디로 회원리스트 검색 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "아이디로 검색 성공"),
            @ApiResponse(responseCode = "404", description = "검색 실패.")
    })
    public ResponseEntity<Page<MemberGetResponseOnlyMember>> listByUser(
            @Validated @RequestBody MemberGetRequestByUserId memberGetRequestByUserId) {

        int page = memberGetRequestByUserId.getPage();
        int size = memberGetRequestByUserId.getSize();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

        return ResponseEntity.ok(memberAdminService.getMemberListByUserId(memberGetRequestByUserId, pageRequestDTO));
    }

    @PostMapping("/email")
    @Operation(summary = "이메일로 회원리스트 검색", description = "이메일로 회원리스트 검색 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일로 검색 성공"),
            @ApiResponse(responseCode = "404", description = "검색 실패.")
    })
    public ResponseEntity<Page<MemberGetResponseOnlyMember>> listByEmail(
            @Validated @RequestBody MemberGetRequestByEmail memberGetRequestByEmail) {

        int page = memberGetRequestByEmail.getPage();
        int size = memberGetRequestByEmail.getSize();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

        return ResponseEntity.ok(memberAdminService.getMemberListByEmail(memberGetRequestByEmail, pageRequestDTO));
    }

    @PostMapping("/nickname")
    @Operation(summary = "닉네임으로 회원리스트 검색", description = "닉네임으로 회원리스트 검색 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임으로 검색 성공"),
            @ApiResponse(responseCode = "404", description = "검색 실패.")
    })
    public ResponseEntity<Page<MemberGetResponseOnlyMember>> listByNickname(
            @Validated @RequestBody MemberGetRequestByNickname memberGetRequestByNickname) {

        int page = memberGetRequestByNickname.getPage();
        int size = memberGetRequestByNickname.getSize();
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

        return ResponseEntity.ok(memberAdminService.getMemberListByNickname(memberGetRequestByNickname, pageRequestDTO));
    }

}
