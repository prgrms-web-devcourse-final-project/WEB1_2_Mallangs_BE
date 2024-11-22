package com.mallangs.domain.member;

import com.mallangs.domain.member.embadded.*;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    public Member(String memberId, String nickname, String password, String email, Address address, String profileImage, Boolean hasPet, PasswordEncoder passwordEncoder) {
        this.memberId = new MemberId(memberId);
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.email = new Email(email);
        this.address1 = address;
        this.profileImage = profileImage;
        this.hasPet = hasPet;
    }
    public void changeNickname(String nickname) {
        this.nickname = new Nickname(nickname);
    }
    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = new Password(password, passwordEncoder);
    }
    public void changeEmail(String email) {
        this.email = new Email(email);
    }
    public void changeAddress1(Address address1) {
        this.address1 = address1;
    }
    public void changeAddress2(Address address2) {
        this.address1 = address2;
    }
    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void changeHaspet(Boolean hasPet) {
        this.hasPet = hasPet;
    }
    public void changeIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
