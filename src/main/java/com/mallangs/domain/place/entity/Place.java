package com.mallangs.domain.place.entity;

import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(length = 50)
    private String name;

    @Column(length = 255)
    private String businessHours;

    @Column(length = 50)
    private String closeDays;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String website; //웹사이트링크
}