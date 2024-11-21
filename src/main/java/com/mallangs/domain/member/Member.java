package com.mallangs.domain.member;

import com.mallangs.domain.member.embadded.*;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberId memberId;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @Embedded
    private Email email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address1_id",nullable = false)
    private Address address1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address2_id")
    private Address address2;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRoles;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "has_pet", nullable = false)
    private Boolean hasPet;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}
