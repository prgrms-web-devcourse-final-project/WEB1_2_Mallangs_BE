package com.mallangs.domain.pet.service;

import com.mallangs.domain.member.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.repository.PetRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class PetService {
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    //본인의 반려동물 목록 조회
    public Page<PetResponse> getAllMyPets(PageRequest pageRequest, Long memberId) {
        try {
            Sort sort = Sort.by("petId").descending();
            Pageable pageable = pageRequest.getPageable(sort);
            Page<Pet> pets = petRepository.findAllPetsByMemberId(memberId, pageable);
            return pets.map(PetResponse::new);
        } catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("getAllMyPets error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_FOUND);
        }
    }

    //반려동물 조회
    public PetResponse getPet(Long petId, Long memberId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));

        //타인의 비활성화 반려동물
        if (!pet.getIsActive() && !pet.getMember().getMemberId().equals(memberId)) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_ACTIVATE);
        }

        //타인의 비공개 반려동물
        if (!pet.getIsOpenProfile() && !pet.getMember().getMemberId().equals(memberId)) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_PROFILE_OPEN);
        }
        return new PetResponse(pet);
    }

    //대표 말랑이(반려동물) 조회
    public PetResponse getRepresentativePet(Long memberId) {
        Pet pet =petRepository.findRepresentativePetByMemberId(memberId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));

        return new PetResponse(pet);
    }

    //반려동물 등록
    public PetResponse createPet(PetCreateRequest petCreateRequest) {
        Member member = getMember(petCreateRequest);

        // 사용자의 첫 반려동물인지 확인
        boolean isFirstPet = !petRepository.existsByMemberId(member.getMemberId());

        try {
            Pet pet = petCreateRequest.toEntity(member);
            pet.changeRepresentative(isFirstPet);

            petRepository.save(pet);

            return new PetResponse(pet);
        } catch (Exception e) {
            log.error("createPet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_CREATE);
        }
    }

    //대표 말랑이(반려동물) 등록
    public void setRepresentativePet(Long memberId, Long petId) {
        try {
            // 기존 대표 반려동물이 있다면 조회하여 대표상태 해제
            petRepository.findRepresentativePetByMemberId(memberId)
                    .ifPresent(currentRepresentative -> currentRepresentative.changeRepresentative(false));

            // 새로운 대표 반려동물 설정
            Pet newRepresentative = petRepository.findById(petId)
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
            newRepresentative.changeRepresentative(true);
        } catch (RuntimeException e) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_REPRESENTATIVE);
        }
    }

    //반려동물 정보 수정
    public PetResponse updatePet(PetUpdateRequest petUpdateRequest, Long petId) {

        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));

            pet.change(
                    petUpdateRequest.getName() != null ? petUpdateRequest.getName() : pet.getName(),
                    petUpdateRequest.getPetType() != null ? petUpdateRequest.getPetType() : pet.getPetType(),
                    petUpdateRequest.getImage() != null ? petUpdateRequest.getImage() : pet.getImage(),
                    petUpdateRequest.getBirthdate() != null ? petUpdateRequest.getBirthdate() : pet.getBirthdate(),
                    petUpdateRequest.getWeight() != null ? petUpdateRequest.getWeight() : pet.getWeight(),
                    petUpdateRequest.getDescription() != null ? petUpdateRequest.getDescription() : pet.getDescription(),
                    petUpdateRequest.getIsOpenProfile() != null ? petUpdateRequest.getIsOpenProfile() : pet.getIsOpenProfile(),
                    petUpdateRequest.getIsNeutering() != null ? petUpdateRequest.getIsNeutering() : pet.getIsNeutering(),
                    petUpdateRequest.getGender() != null ? petUpdateRequest.getGender() : pet.getGender()
            );
            petRepository.save(pet);
            return new PetResponse(pet);

        } catch (Exception e) {
            log.error("updatePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_UPDATE);
        }
    }

    //반려동물 삭제 (비활성화)
    private PetResponse deletePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
            pet.deactivate();
            petRepository.save(pet);
            return new PetResponse(pet);
        } catch (Exception e) {
            log.error("deletePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_DELETE);
        }
    }

    //반려동물 복원 (활성화)
    private PetResponse restorePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분
            pet.activate();
            petRepository.save(pet);
            return new PetResponse(pet);
        } catch (Exception e) {
            log.error("restorePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_RESTORE);
        }
    }

    //반경 내 반려동물 조회
    public Page<PetResponse> getNearbyPets(PetLocationRequest petLocationRequest,
                                           PageRequest pageRequest) {
        try {
            validateLocationSearch(petLocationRequest);

            Sort sort = Sort.by("petId").descending();
            Pageable pageable = pageRequest.getPageable(sort);

            // 지역명 필터 추가
            Page<Pet> pets = petRepository.findNearbyPets(
                    petLocationRequest.getY(),    // latitude는 y좌표로 매핑
                    petLocationRequest.getX(),    // longitude는 x좌표로 매핑
                    petLocationRequest.getRadius(),
                    petLocationRequest.getRegion1depthName(),  // 시/도
                    petLocationRequest.getRegion2depthName(),  // 구/군
                    petLocationRequest.getRegion3depthName(),  // 동/읍/면
                    pageable
            );

            return pets.map(pet -> {
                PetResponse dto = new PetResponse(pet);
                // 거리 계산 추가
                double distance = calculateDistance(
                        petLocationRequest.getY(),  // latitude -> y
                        petLocationRequest.getX(),  // longitude -> x
                        pet.getMember().getAddress1().getY(),
                        pet.getMember().getAddress1().getX()
                );
                dto.assignDistance(Math.round(distance * 100.0) / 100.0); // 소수점 2자리까지
                return dto;
            });

        } catch (Exception e) {
            log.error("getNearbyPets error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_SEARCH_LOCATION);
        }
    }



    // 위치 검색 파라미터 검증
    private void validateLocationSearch(PetLocationRequest searchDTO) {
        if (searchDTO.getX() == null ||
                searchDTO.getY() == null ||
                searchDTO.getRadius() == null) {
            throw new MallangsCustomException(ErrorCode.LOCATION_INVALIDE_PARAMS);
        }

        if (searchDTO.getY() < -90 || searchDTO.getY() > 90 ||
                searchDTO.getX() < -180 || searchDTO.getX() > 180 ||
                searchDTO.getRadius() <= 0 || searchDTO.getRadius() > 20) { // 최대 반경 20km
            throw new MallangsCustomException(ErrorCode.LOCATION_INVALIDE_RANGE);
        }
    }

    // Haversine 공식을 사용한 두 지점 간의 거리 계산 (km)
    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {
        final int R = 6371; // 지구의 반경 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private Member getMember(PetCreateRequest petCreateRequest) {

        return memberRepository.findById(petCreateRequest.getMemberId()).orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


}
