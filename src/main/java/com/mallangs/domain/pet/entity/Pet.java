package com.mallangs.domain.pet.entity;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(columnDefinition = "TEXT")
    private String image;

    private LocalDate birthdate;

    private Double weight;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean isOpenProfile;

    @Column(nullable = false)
    private Boolean isNeutering;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    @Column(nullable = false)
    private Boolean isActive = true; // 기본값은 true

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changePetType(PetType petType){
        this.name = name;
    }

    public void changeImage(String image) {
        this.image = image;
    }

    public void changeBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void changeWeight(Double weight) {
        this.weight = weight;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeIsOpenProfile(Boolean isOpenProfile) {
        this.isOpenProfile = isOpenProfile;
    }

    public void changeIsNeutering(Boolean isNeutering) {
        this.isNeutering = isNeutering;
    }

    public void changeGender(PetGender gender) {
        this.gender = gender;
    }
}