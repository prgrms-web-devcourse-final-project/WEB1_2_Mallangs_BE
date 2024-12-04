package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.MemberAddressRequest;
import com.mallangs.domain.member.dto.MemberAddressResponse;
import com.mallangs.domain.member.entity.embadded.QUserId;
import com.mallangs.domain.member.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/address")
@Tag(name = "주소", description = "주소 CRUD")
public class AddressController {

    public final AddressService addressService;

    @PostMapping
    @Operation(summary = "주소등록", description = "주소등록 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "주소입력을 잘못하였습니다.")
    })
    public ResponseEntity<?> create(@Validated @RequestBody MemberAddressRequest memberAddressRequest,
                                    Authentication authentication) {
        String userId = authentication.getName();
        addressService.create(userId, memberAddressRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "주소조회", description = "주소조회 요청 API")
    public ResponseEntity<List<MemberAddressResponse>> get(@RequestParam("memberId") Long memberId) {

        List<MemberAddressResponse> addresses = addressService.get(memberId);
        if (addresses == null || addresses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "주소삭제", description = "주소삭제 요청 API")
    public ResponseEntity<?> delete(@PathVariable("addressId") Long addressId) {
        boolean result = addressService.delete(addressId);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok("삭제성공");
    }
}
