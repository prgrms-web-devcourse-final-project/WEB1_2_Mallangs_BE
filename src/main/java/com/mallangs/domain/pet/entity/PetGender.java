package com.mallangs.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetGender {
    MALE("남아"),
    FEMALE("여아"),
    ETC("모름");

    private final String koreanName;

    PetGender(String koreanName) {
        this.koreanName = koreanName;
    }

}