package com.mallangs.domain.member;

import com.mallangs.domain.member.embadded.Email;
import com.mallangs.domain.member.embadded.Nickname;
import com.mallangs.domain.member.embadded.Password;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Password password;

    @Embedded
    private Email email;

    @ManyToOne
    private Address address1;

    @ManyToOne
    private Address address2;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "member_id")
//    private List<MemberRole> roleList;
}
