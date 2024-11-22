package com.mallangs.domain.pet.service;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.repository.PetRepository;
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
    public Page<PetResponseDTO> getAllMyPets(PageRequestDTO pageRequestDTO, Long memberId) {
        try {
            Sort sort = Sort.by("petId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            Page<Pet> pets = petRepository.findAllPetsByMemberId(memberId, pageable);
            return pets.map(PetResponseDTO::new);
        } catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("getAllMyPets error: {}", e.getMessage());
            throw new RuntimeException("getAllMyPets error");
        }
    }

    //반려동물 조회
    public PetResponseDTO getPet(Long petId, Long memberId) {
        Pet pet = petRepository.findById(petId).orElseThrow(EntityNotFoundException::new); //Merge시 예외 처리 커스텀

        if (!pet.getIsActive() && !pet.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("Pet ID: " + petId + " 비활성화 상태입니다.");//Merge시 예외 처리 커스텀으로 변경
        }

        if (!pet.getIsOpenProfile() && !pet.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("Pet profile Id: " + petId + " 프로필 비공개 상태 입니다.");//Merge시 예외 처리 커스텀으로 변경
        }

        return new PetResponseDTO(pet);

    }

    //반려동물 등록
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        Member member = getMember(petRequestDTO);

        try {
            Pet pet = petRequestDTO.toEntity(member);
            petRepository.save(pet);

            return new PetResponseDTO(pet);
        } catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("createPet error: {}", e.getMessage());
            throw new RuntimeException("createPet error");
        }
    }

    //반려동물 정보 수정
    public PetResponseDTO updatePet(PetUpdateDTO petUpdateDTO, Long petId) {

        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분

            if (petUpdateDTO.getName() != null) {
                pet.changeName(petUpdateDTO.getName());
            }
            if (petUpdateDTO.getPetType() != null) {
                pet.changePetType(petUpdateDTO.getPetType());
            }
            if (petUpdateDTO.getImage() != null) {
                pet.changeImage(petUpdateDTO.getImage());
            }
            if (petUpdateDTO.getBirthdate() != null) {
                pet.changeBirthdate(petUpdateDTO.getBirthdate());
            }
            if (petUpdateDTO.getWeight() != null) {
                pet.changeWeight(petUpdateDTO.getWeight());
            }
            if (petUpdateDTO.getDescription() != null) {
                pet.changeDescription(petUpdateDTO.getDescription());
            }
            if (petUpdateDTO.getIsOpenProfile() != null) {
                pet.changeIsOpenProfile(petUpdateDTO.getIsOpenProfile());
            }
            if (petUpdateDTO.getIsNeutering() != null) {
                pet.changeIsNeutering(petUpdateDTO.getIsNeutering());
            }
            if (petUpdateDTO.getGender() != null) {
                pet.changeGender(petUpdateDTO.getGender());
            }

            petRepository.save(pet);
            return new PetResponseDTO(pet);

        } catch (Exception e) {
            // 예외처리 구축시 커스텀 예외 적용
            log.error("updatePet error: {}", e.getMessage());
            throw new RuntimeException("updatePet error");
        }
    }

    //반려동물 삭제 (비활성화)
    private PetResponseDTO deletePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분
            pet.deactivate();
            petRepository.save(pet);
            return new PetResponseDTO(pet);
        } catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("deletePet error: {}", e.getMessage());
            throw new RuntimeException("deletePet error");
        }
    }

    //반려동물 복원 (활성화)
    private PetResponseDTO restorePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분
            pet.activate();
            petRepository.save(pet);
            return new PetResponseDTO(pet);
        } catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("restorePet error: {}", e.getMessage());
            throw new RuntimeException("deletePet error");
        }
    }

    //반경 내 반려동물 조회
    public Page<PetResponseDTO> getNearbyPets(PetLocationDTO petLocationDTO,
                                              PageRequestDTO pageRequestDTO) {
        try {
            validateLocationSearch(petLocationDTO);

            Sort sort = Sort.by("petId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            // 지역명 필터 추가
            Page<Pet> pets = petRepository.findNearbyPets(
                    petLocationDTO.getY(),    // latitude는 y좌표로 매핑
                    petLocationDTO.getX(),    // longitude는 x좌표로 매핑
                    petLocationDTO.getRadius(),
                    petLocationDTO.getRegion1depthName(),  // 시/도
                    petLocationDTO.getRegion2depthName(),  // 구/군
                    petLocationDTO.getRegion3depthName(),  // 동/읍/면
                    pageable
            );

            return pets.map(pet -> {
                PetResponseDTO dto = new PetResponseDTO(pet);
                // 거리 계산 추가
                double distance = calculateDistance(
                        petLocationDTO.getY(),  // latitude -> y
                        petLocationDTO.getX(),  // longitude -> x
                        pet.getMember().getAddress1().getY(),
                        pet.getMember().getAddress1().getX()
                );
                dto.assignDistance(Math.round(distance * 100.0) / 100.0); // 소수점 2자리까지
                return dto;
            });

        } catch (Exception e) {
            log.error("getNearbyPets error: {}", e.getMessage());
            //throw new CustomException(ErrorCode.LOCATION_SEARCH_ERROR);
            throw new RuntimeException("LOCATION_SEARCH_ERROR");
        }
    }



    // 위치 검색 파라미터 검증
    private void validateLocationSearch(PetLocationDTO searchDTO) {
        if (searchDTO.getX() == null ||
                searchDTO.getY() == null ||
                searchDTO.getRadius() == null) {
            //throw new CustomException(ErrorCode.INVALID_LOCATION_PARAMS);
            throw new RuntimeException("INVALID_LOCATION_PARAMS");
        }

        if (searchDTO.getY() < -90 || searchDTO.getY() > 90 ||
                searchDTO.getX() < -180 || searchDTO.getX() > 180 ||
                searchDTO.getRadius() <= 0 || searchDTO.getRadius() > 20) { // 최대 반경 20km
            //throw new CustomException(ErrorCode.INVALID_LOCATION_RANGE);
            throw new RuntimeException("INVALID_LOCATION_RANGE");
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

    private Member getMember(PetRequestDTO petRequestDTO) {
        Member member = memberRepository.findById(petRequestDTO.getMemberId()).orElse(null);//머지시 커스텀 예외로 대체될 부분

        //예외처리 merge시 적용
//        if (member == null) {
//            throw MemberException.NOT_FOUND.get();
//        }
        return member;
    }


}
