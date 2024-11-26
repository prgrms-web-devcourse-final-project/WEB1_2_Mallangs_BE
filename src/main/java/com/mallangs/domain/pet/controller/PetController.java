package com.mallangs.domain.pet.controller;

import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "반려동물(말랑이)", description = "반려동물 CRUD")
public class PetController {

    private final PetService petService;

    //유저 본인 반려동물 전체 조회
    @Operation(summary = "유저 본인의 반려동물 페이지 조회", description = "유저 본인의 말랑이 목록을 페이지로 조회할때 사용하는 API")
    @GetMapping
    public ResponseEntity<Page<PetResponse>> getAllMyPets(
            @ModelAttribute PageRequest pageRequestDTO,
            @AuthenticationPrincipal Long memberId) {
        Page<PetResponse> pets = petService.getAllMyPets(pageRequestDTO, memberId);
        return ResponseEntity.ok(pets);
    }

    //반려동물 조회
    @Operation(summary = "반려동물 정보조회", description = "반려동물의 정보를 조회하는 API")
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getPet(
            @PathVariable Long petId,
            @AuthenticationPrincipal Long memberId) {
        PetResponse pet = petService.getPet(petId, memberId);
        return ResponseEntity.ok(pet);
    }

    //대표 말랑이(반려동물 조회)
    @GetMapping("/representative")
    @Operation(summary = "대표 반려동물(말랑이) 정보조회", description = "대표 반려동물(말랑이) 정보를 조회하는 API")
    public ResponseEntity<PetResponse> getRepresentativePet(
            @AuthenticationPrincipal Long memberId) {
        PetResponse pet = petService.getRepresentativePet(memberId);
        return ResponseEntity.ok(pet);
    }

    //반려동물 등록
    @PostMapping
    @Operation(summary = "반려동물 등록", description = "반려동물을 등록하는 API")
    public ResponseEntity<PetResponse> createPet(
            @RequestBody PetCreateRequest petCreateRequest) {
        PetResponse pet = petService.createPet(petCreateRequest);
        return ResponseEntity.ok(pet);
    }

    //대표 말랑이 변경
    @PutMapping("/representative/{petId}")
    @Operation(summary = "대표 반려동물(말랑이) 변경", description = "대표 반려동물(말랑이)을 변경하는 API")
    public ResponseEntity<Void> updateRepresentativePet(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long petId) {
        petService.setRepresentativePet(memberId, petId);
        return ResponseEntity.ok().build();
    }

    //반려동물 정보수정
    @PatchMapping("/{petId}")
    @Operation(summary = "반려동물 정보 수정", description = "반려동물을 정보를 수정하는 API")
    public ResponseEntity<PetResponse> updatePet(
            @RequestBody PetUpdateRequest petUpdateDTO,
            @PathVariable Long petId) {
        PetResponse pet = petService.updatePet(petUpdateDTO, petId);
        return ResponseEntity.ok(pet);
    }
    //근처 반려동물 조회
    @GetMapping("/nearby")
    @Operation(summary = "근처의 반려동물 목록을 조회", description = "클라이언트로부터 위도 경도 ,시/군/구 등의 데이터를 받았을때 위도 경도를 기준으로 거리를 계산하여 반경내(20km)의 반려동물 목록을 조회")
    public ResponseEntity<Page<PetResponse>> getNearbyPets(
            @ModelAttribute PetLocationRequest petLocationDTO,
            @ModelAttribute PageRequest pageRequestDTO) {
        Page<PetResponse> pets = petService.getNearbyPets(petLocationDTO, pageRequestDTO);
        return ResponseEntity.ok(pets);
    }

}
