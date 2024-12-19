package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.dto.AddressCreateSuccessResponse;
import com.mallangs.domain.member.dto.AddressDeleteSuccessResponse;
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
            @ApiResponse(responseCode = "201", description = "주소 등록 성공"),
            @ApiResponse(responseCode = "400", description = "주소입력을 확인해주세요.")
    })
    public ResponseEntity<AddressCreateSuccessResponse> create(@Validated @RequestBody MemberAddressRequest memberAddressRequest,
                                                               Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(userId, memberAddressRequest));
    }

    @GetMapping
    @Operation(summary = "주소조회", description = "주소조회 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "즈소 조회 성공"),
            @ApiResponse(responseCode = "400", description = "즈소번호 입력을 확인해주세요.")
    })
    public ResponseEntity<List<MemberAddressResponse>> get(@RequestParam("memberId") Long memberId) {

        List<MemberAddressResponse> addresses = addressService.get(memberId);
        if (addresses == null || addresses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addresses);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "주소삭제", description = "주소삭제 요청 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "즈소 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "즈소번호 입력을 확인해주세요.")
    })
    public ResponseEntity<AddressDeleteSuccessResponse> delete(@PathVariable("addressId") Long addressId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.delete(addressId));
    }
}
