package com.mallangs.domain.pet.service;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.PageRequestDTO;
import com.mallangs.domain.pet.dto.PetRequestDTO;
import com.mallangs.domain.pet.dto.PetResponseDTO;
import com.mallangs.domain.pet.dto.PetUpdateDTO;
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
        }catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("getAllMyPets error: {}",  e.getMessage());
            throw new RuntimeException("getAllMyPets error");
        }
    }

    //반려동물 조히
    public PetResponseDTO getMyPet(Long petId) {
      Pet pet = petRepository.findById(petId).orElseThrow(EntityNotFoundException::new); //Merge시 예외 처리 커스텀
      return new PetResponseDTO(pet);
    }



    //반려동물 등록
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        Member member = getMember(petRequestDTO);

        try {
            Pet pet = petRequestDTO.toEntity(member);
            petRepository.save(pet);

            return new PetResponseDTO(pet);
        }catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("createPet error: {}",  e.getMessage());
            throw new RuntimeException("createPet error");
        }
    }

    //반려동물 정보 수정
    public PetResponseDTO updatePet(PetUpdateDTO petUpdateDTO, Long petId) {

        try{
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
            // Handle exceptions appropriately
            log.error("updatePet error: {}", e.getMessage());
            throw new RuntimeException("updatePet error");
        }
    }

    private PetResponseDTO deletePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분
            pet.deactivate();
            petRepository.save(pet);
            return new PetResponseDTO(pet);
        }catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("deletePet error: {}",  e.getMessage());
            throw new RuntimeException("deletePet error");
        }
    }

    private PetResponseDTO restorePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));//머지시 커스텀 예외로 대체될 부분
            pet.activate();
            petRepository.save(pet);
            return new PetResponseDTO(pet);
        }catch (Exception e) {
            //예외처리 merge 되면 커스텀 예외 생성
            log.error("restorePet error: {}",  e.getMessage());
            throw new RuntimeException("deletePet error");
        }
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
