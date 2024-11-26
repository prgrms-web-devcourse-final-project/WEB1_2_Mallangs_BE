package com.mallangs.domain.member.entity;

import com.mallangs.domain.member.entity.embadded.*;
import com.mallangs.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@ToString(exclude = "addresses")
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberRole memberRole = MemberRole.ROLE_USER;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "has_pet", nullable = false)
    private Boolean hasPet;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // 회원가입
    public Member(String userId, String nickname, Password password, String email, String profileImage, Boolean hasPet){
        this.userId = new UserId(userId);
        this.nickname = new Nickname(nickname);
        this.password = password;
        this.email = new Email(email);
        this.profileImage = profileImage;
        this.hasPet = hasPet;
    }

    //수정
    public void change(String nickname,String password, String email, String profileImage, PasswordEncoder passwordEncoder){
        this.nickname = new Nickname(nickname);
        this.password = new Password(password, passwordEncoder);
        this.email = new Email(email);
        this.profileImage = profileImage;
    }

    public void changeIsActive(Boolean isActive){
        this.isActive = isActive;
    }

    public void changePassword(Password password){
        this.password = password;
    }

    public void addAddress(Address address){
        this.addresses.add(address);
    }

    public void removeAddress(Address address){
        this.addresses.remove(address);
    }

}
