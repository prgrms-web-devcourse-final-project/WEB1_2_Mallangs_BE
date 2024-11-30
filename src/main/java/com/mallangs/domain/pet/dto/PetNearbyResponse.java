package com.mallangs.domain.pet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PetNearbyResponse {

    private Long petId;
    private Long memberId;
    private String name;
    private PetType petType;
    private String image;
    private LocalDate birthdate;
    private PetGender gender;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private Double distance; //거리정보

    // Entity -> DTO
    public PetNearbyResponse(Pet pet) {
        this.petId = pet.getPetId();
        this.memberId = pet.getMember().getMemberId();
        this.birthdate = pet.getBirthdate();
        this.name = pet.getName();
        this.petType = pet.getPetType();
        this.image = pet.getImage();
        this.gender = pet.getGender();
        this.createdAt = pet.getCreatedAt();
        this.updatedAt = pet.getUpdatedAt();
    }

//     거리 정보를 설정하는 비즈니스 메서드
    public void assignDistance(Double distance) {
        this.distance = distance;
    }
}