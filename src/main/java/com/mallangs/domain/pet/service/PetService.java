package com.mallangs.domain.pet.service;

import com.mallangs.domain.jwt.entity.CustomMemberDetails;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.repository.PetRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    // memberId는 member 객체 기본키, userId는 로그인시 아이디
    public Page<PetResponse> getAllMyPets(PageRequest pageRequest, CustomMemberDetails customMemberDetails) {
        Member member = getMember(customMemberDetails);
        try {
            Sort sort = Sort.by("petId").descending();
            Pageable pageable = pageRequest.getPageable(sort);
            Page<Pet> pets = petRepository.findAllPetsByMemberId(member.getMemberId(), pageable);
            return pets.map(PetResponse::new);
        } catch (Exception e) {
            log.error("getAllMyPets error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_FOUND);
        }
    }

    //반려동물 조회
    public PetResponse getPet(Long petId, CustomMemberDetails customMemberDetails) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
        Member member = getMember(customMemberDetails);
        //타인의 비활성화 반려동물 조회시 예외 던짐
        if (!pet.getIsActive() && !pet.getMember().getMemberId().equals(member.getMemberId())) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_ACTIVATE);
        }

        //타인의 비공개 반려동물 조회시 예외 던짐
        if (!pet.getIsOpenProfile() && !pet.getMember().getMemberId().equals(member.getMemberId())) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_PROFILE_OPEN);
        }
        return new PetResponse(pet);
    }

    //대표 말랑이(반려동물) 조회
    public PetResponse getRepresentativePet(CustomMemberDetails customMemberDetails) {
        Member member = getMember(customMemberDetails);
        Pet pet =petRepository.findRepresentativePetByMemberId(member.getMemberId()).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));

        return new PetResponse(pet);
    }

    //반려동물 등록
    public PetResponse createPet(PetCreateRequest petCreateRequest, CustomMemberDetails customMemberDetails) {
        Member member = getMember(customMemberDetails);

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
    public void createRepresentativePet(CustomMemberDetails customMemberDetails, Long petId) {
        Member member = getMember(customMemberDetails);
        try {
            // 기존 대표 반려동물이 있다면 조회하여 대표상태 해제
            petRepository.findRepresentativePetByMemberId(member.getMemberId())
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
    public PetResponse updatePet(PetUpdateRequest petUpdateRequest, Long petId, CustomMemberDetails customMemberDetails) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
        Member member = getMember(customMemberDetails);

        //반려동물 수정시도시 예외 던짐
        if (!pet.getMember().getMemberId().equals(member.getMemberId())) {
            throw new MallangsCustomException(ErrorCode.PET_NOT_OWNED);
        }

        try {
            pet.change(
                    petUpdateRequest.getName() != null ? petUpdateRequest.getName() : pet.getName(),
                    petUpdateRequest.getPetType() != null ? petUpdateRequest.getPetType() : pet.getPetType(),
                    petUpdateRequest.getImage() != null ? petUpdateRequest.getImage() : pet.getImage(),
                    petUpdateRequest.getBirthdate() != null ? petUpdateRequest.getBirthdate() : pet.getBirthdate(),
                    petUpdateRequest.getWeight() != null ? petUpdateRequest.getWeight() : pet.getWeight(),
                    petUpdateRequest.getDescription() != null ? petUpdateRequest.getDescription() : pet.getDescription(),
                    petUpdateRequest.getIsOpenProfile() != null ? petUpdateRequest.getIsOpenProfile() : pet.getIsOpenProfile(),
                    petUpdateRequest.getIsNeutering() != null ? petUpdateRequest.getIsNeutering() : pet.getIsNeutering(),
                    petUpdateRequest.getGender() != null ? petUpdateRequest.getGender() : pet.getGender(),
                    petUpdateRequest.getMicroChip() != null ? petUpdateRequest.getMicroChip() : pet.getMicroChip()
            );
            petRepository.save(pet);
            return new PetResponse(pet);

        } catch (RuntimeException e) {
            log.error("updatePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_UPDATE);
        }
    }

    //반려동물 삭제 (비활성화)
    public PetResponse deletePet(Long petId, CustomMemberDetails customMemberDetails) {
        Member member = getMember(customMemberDetails);
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
            if (!pet.getMember().getMemberId().equals(member.getMemberId())) {
                throw new MallangsCustomException(ErrorCode.PET_NOT_OWNED);
            }
            pet.deactivate();
            petRepository.save(pet);
            return new PetResponse(pet);
        } catch (RuntimeException e) {
            log.error("deletePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_DELETE);
        }
    }

    //반려동물 복원 (활성화)
    public PetResponse restorePet(Long petId, CustomMemberDetails customMemberDetails) {
        Member member = getMember(customMemberDetails);
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
            if (!pet.getMember().getMemberId().equals(member.getMemberId())) {
                throw new MallangsCustomException(ErrorCode.PET_NOT_OWNED);
            }
            pet.activate();
            petRepository.save(pet);
            return new PetResponse(pet);
        } catch (RuntimeException e) {
            log.error("restorePet error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_RESTORE);
        }
    }

    //반경 내 반려동물 조회
    public Page<PetNearbyResponse> getNearbyPets(PetLocationRequest petLocationRequest,
                                           PageRequest pageRequest) {
        try {
            validateLocationSearch(petLocationRequest);

            Sort sort = Sort.by("pet_Id").descending();
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
                PetNearbyResponse dto = new PetNearbyResponse(pet);
                // 거리 계산 추가
                double distance = calculateDistance(
                        petLocationRequest.getY(),  // latitude -> y
                        petLocationRequest.getX(),  // longitude -> x
                        pet.getMember().getAddresses().isEmpty() ? 0.0 : pet.getMember().getAddresses().get(0).getPoint().getY(),
                        pet.getMember().getAddresses().isEmpty() ? 0.0 : pet.getMember().getAddresses().get(0).getPoint().getX()
                );
                dto.assignDistance(Math.round(distance * 100.0) / 100.0); // 소수점 2자리까지
                return dto;
            });

        } catch (RuntimeException e) {
            log.error("getNearbyPets error: {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.PET_NOT_SEARCH_LOCATION);
        }
    }

    // 위치 검색 파라미터 검증
    private void validateLocationSearch(PetLocationRequest petLocationRequest) {
        if (petLocationRequest.getX() == null || petLocationRequest.getY() == null || petLocationRequest.getRadius() == null) {
            throw new MallangsCustomException(ErrorCode.LOCATION_INVALIDE_PARAMS);
        }

          double x = petLocationRequest.getX();
          double y = petLocationRequest.getY();

        if (y < -90 || y > 90 || //북위는 양수로 남위는 음수로 표현
                x < -180 || x > 180 || //동경은 양수로 서경은 음수로 표현
                petLocationRequest.getRadius() <= 0 || petLocationRequest.getRadius() > 20) { // 최대 반경 20km
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

    private Member getMember(CustomMemberDetails customMemberDetails) {
        UserId userId = new UserId(customMemberDetails.getUserId());

        return memberRepository.findByUserId(userId).orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


}
