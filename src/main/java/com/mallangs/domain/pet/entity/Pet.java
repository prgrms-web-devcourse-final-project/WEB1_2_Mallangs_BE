package com.mallangs.domain.pet.entity;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(columnDefinition = "TEXT")
    private String image;

    private LocalDate birthdate;

    private Double weight;

    @Column(nullable = false)
    private Boolean isNeutering;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

}