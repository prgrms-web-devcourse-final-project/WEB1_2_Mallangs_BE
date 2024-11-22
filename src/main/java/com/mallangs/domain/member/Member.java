package com.mallangs.domain.member;

import com.mallangs.domain.member.embadded.*;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@Entity
@EqualsAndHashCode(of = "memberId", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Embedded
    private UserId userId;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @Embedded
    private Email email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address1_id", nullable = false)
    private Address address1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address2_id")
    private Address address2;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRoles = MemberRole.ROLE_USER;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "has_pet", nullable = false)
    private Boolean hasPet;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // 회원가입
    public Member(String userId, String nickname, String password, String email, Address address, String profileImage, Boolean hasPet, PasswordEncoder passwordEncoder) {
        this.userId = new UserId(userId);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.email = new Email(email);
        this.address1 = address;
        this.profileImage = profileImage;
        this.hasPet = hasPet;
    }

    @Builder
    public void change(String nickname,String password, String email, Address address1, Address address2, String profileImage,Boolean hasPet,Boolean isActive, PasswordEncoder passwordEncoder){
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.email = new Email(email);
        this.address1 = address1;
        this.address2 = address2;
        this.profileImage = profileImage;
        this.hasPet = hasPet;
        this.isActive = isActive;
    }

}
