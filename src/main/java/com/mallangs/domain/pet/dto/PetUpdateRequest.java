package com.mallangs.domain.pet.dto;

import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PetUpdateRequest {
    private String name;
    private PetType petType;
    private String image;
    private LocalDate birthdate;
    private Double weight;
    private String description;
    private Boolean isOpenProfile;
    private Boolean isNeutering;
    private PetGender gender;
    private String microChip;
}