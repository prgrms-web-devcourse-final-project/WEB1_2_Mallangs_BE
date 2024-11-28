package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.MemberAddressRequest;
import com.mallangs.domain.member.dto.MemberAddressResponse;
import com.mallangs.domain.member.entity.embadded.QUserId;
import com.mallangs.domain.member.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/address")
@Tag(name = "주소", description = "주소 CRUD")
public class AddressController {

    public final AddressService addressService;

    @PostMapping
    @Operation(summary = "주소등록", description = "주소등록 요청 API")
    public ResponseEntity<?> create(@Validated @RequestBody MemberAddressRequest memberAddressRequest,
                                    Authentication authentication) {
        String userId = authentication.getName();
        log.info("userId: {}", userId);
        addressService.create(userId, memberAddressRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "주소조회", description = "주소조회 요청 API")
    public ResponseEntity<List<MemberAddressResponse>> get(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(addressService.get(memberId));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "주소삭제", description = "주소삭제 요청 API")
    public ResponseEntity<?> delete(@PathVariable("addressId") Long addressId) {
        addressService.delete(addressId);
        return ResponseEntity.ok().build();
    }
}
