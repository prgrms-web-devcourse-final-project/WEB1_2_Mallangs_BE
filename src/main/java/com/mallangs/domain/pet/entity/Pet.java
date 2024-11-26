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

    @Column(name = "is_open_profile", nullable = false)
    private Boolean isOpenProfile;

    @Column(nullable = false)
    private Boolean isNeutering;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    private boolean isRepresentative;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true; // 기본값은 true

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void changeRepresentative(boolean isRepresentative){
        this.isRepresentative = isRepresentative;
    }

    public void change(String name,
                       PetType petType,
                       String image,
                       LocalDate birthdate,
                       Double weight,
                       String description,
                       Boolean isOpenProfile,
                       Boolean isNeutering,
                       PetGender gender) {
        this.name = name;
        this.petType = petType;
        this.image = image;
        this.birthdate = birthdate;
        this.weight = weight;
        this.description = description;
        this.isOpenProfile = isOpenProfile;
        this.isNeutering = isNeutering;
        this.gender = gender;
    }
}