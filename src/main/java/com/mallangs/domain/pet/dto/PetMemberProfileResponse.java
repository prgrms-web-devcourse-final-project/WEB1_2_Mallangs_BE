package com.mallangs.domain.pet.dto;

import com.mallangs.domain.pet.entity.Pet;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
public class PetMemberProfileResponse {

    private Long petId;
    private String name;
    private String petType;
    private String image;
    private LocalDate birthdate;
    private Double weight;
    private String description;
    private Boolean isOpenProfile;
    private Boolean isNeutering;
    private String gender;
    private Boolean isRepresentative;
    private String microChip;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    // 엔티티 -> DTO 변환 생성자
    public PetMemberProfileResponse(Pet pet) {
        this.petId = pet.getPetId();
        this.name = pet.getName();
        this.petType = pet.getPetType().name();
        this.image = pet.getImage();
        this.birthdate = pet.getBirthdate();
        this.weight = pet.getWeight();
        this.description = pet.getDescription();
        this.isOpenProfile = pet.getIsOpenProfile();
        this.isNeutering = pet.getIsNeutering();
        this.gender = pet.getGender().name();
        this.isRepresentative = pet.getIsRepresentative();
        this.microChip = pet.getMicroChip();
        this.isActive = pet.getIsActive();
        this.createdDate = pet.getCreatedAt();
        this.lastModifiedDate = pet.getUpdatedAt();
    }

}
