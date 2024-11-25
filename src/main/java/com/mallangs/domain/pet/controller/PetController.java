package com.mallangs.domain.pet.controller;

import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    //본인 반려동물 전체 조회
    @GetMapping
    public ResponseEntity<Page<PetResponse>> getAllMyPets(
            @ModelAttribute PageRequest pageRequestDTO,
            @AuthenticationPrincipal Long memberId) {
        Page<PetResponse> pets = petService.getAllMyPets(pageRequestDTO, memberId);
        return ResponseEntity.ok(pets);
    }

    //반려동물 조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getPet(
            @PathVariable Long petId,
            @AuthenticationPrincipal Long memberId) {
        PetResponse pet = petService.getPet(petId, memberId);
        return ResponseEntity.ok(pet);
    }

    //대표 말랑이(반려동물 조회)
    @GetMapping("/representative")
    public ResponseEntity<PetResponse> getRepresentativePet(
            @AuthenticationPrincipal Long memberId) {
        PetResponse pet = petService.getRepresentativePet(memberId);
        return ResponseEntity.ok(pet);
    }

    //반려동물 등록
    @PostMapping
    public ResponseEntity<PetResponse> createPet(
            @RequestBody PetCreateRequest petCreateRequest) {
        PetResponse pet = petService.createPet(petCreateRequest);
        return ResponseEntity.ok(pet);
    }

    //대표 말랑이 변경
    @PutMapping("/representative/{petId}")
    public ResponseEntity<Void> setRepresentativePet(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long petId) {
        petService.setRepresentativePet(memberId, petId);
        return ResponseEntity.ok().build();
    }

    //반려동물 정보수정
    @PatchMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(
            @RequestBody PetUpdateRequest petUpdateDTO,
            @PathVariable Long petId) {
        PetResponse pet = petService.updatePet(petUpdateDTO, petId);
        return ResponseEntity.ok(pet);
    }
    //근처 반려동물 조회
    @GetMapping("/nearby")
    public ResponseEntity<Page<PetResponse>> getNearbyPets(
            @ModelAttribute PetLocationRequest petLocationDTO,
            @ModelAttribute PageRequest pageRequestDTO) {
        Page<PetResponse> pets = petService.getNearbyPets(petLocationDTO, pageRequestDTO);
        return ResponseEntity.ok(pets);
    }

}
