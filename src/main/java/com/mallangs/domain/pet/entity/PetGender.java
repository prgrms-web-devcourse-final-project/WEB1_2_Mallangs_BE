package com.mallangs.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetGender {
    MALE("수컷"),
    FEMALE("암컷");

    private final String koreanName;

    PetGender(String koreanName) {
        this.koreanName = koreanName;
    }
}