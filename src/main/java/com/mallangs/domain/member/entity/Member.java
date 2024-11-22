package com.mallangs.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address1_id", nullable = false)
    private Address address1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address2_id")
    private Address address2;
}
