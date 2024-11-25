package com.mallangs.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetType {
    DOG("강아지"),
    CAT("고양이"),
    BIRD("새"),
    OTHER("기타");

    private final String koreanName;

    PetType(String koreanName) {
        this.koreanName = koreanName;


    }
}