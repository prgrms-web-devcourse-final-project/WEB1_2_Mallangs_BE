package com.mallangs.domain.pet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PetResponseDTO {

    private Long petId;
    private Long memberId;
    private String name;
    private PetType petType;
    private String image;
    private LocalDate birthdate;
    private Double weight;
    private String description;
    private Boolean isProfileOpen;
    private Boolean isNeutering;
    private PetGender gender;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    // Constructor to create DTO from Entity
    public PetResponseDTO(Pet pet) {
        this.petId = pet.getPetId();
        this.memberId=pet.getMember().getMemberId();
        this.name = pet.getName();
        this.petType = pet.getPetType();
        this.image = pet.getImage();
        this.birthdate = pet.getBirthdate();
        this.weight = pet.getWeight();
        this.description = pet.getDescription();
        this.isProfileOpen = pet.getIsOpenProfile();
        this.isNeutering = pet.getIsNeutering();
        this.gender = pet.getGender();
        this.createdAt=pet.getCreatedAt();
        this.updatedAt=pet.getUpdatedAt();
    }
}