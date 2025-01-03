package com.mallangs.domain.pet.dto;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetCreateRequest {
    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotNull(message = "종류를 입력하세요")
    private PetType petType;

    private String image;

    private LocalDate birthdate;

    private Double weight;

    private String description;

    private String microChip;

    //private Boolean isRepresentative;

    @NotNull(message = "프로필 공개 유무를 입력하세요")
    private Boolean isOpenProfile;

    @NotNull(message = "중성화 수술 여부를 입력하세요")
    private Boolean isNeutering;

    @NotNull(message = "성별을 입력하세요")
    private PetGender gender;

    public Pet toEntity(Member member) {
        return Pet.builder()
                .member(member)
                .name(this.name)
                .petType(this.petType)
                .image(this.image)
                .birthdate(this.birthdate)
                .weight(this.weight)
                .description(this.description)
                .isOpenProfile(this.isOpenProfile)
                .isNeutering(this.isNeutering)
                .gender(this.gender)
                //.isRepresentative(this.isRepresentative != null ? this.isRepresentative : false)
                .build();
    }
}