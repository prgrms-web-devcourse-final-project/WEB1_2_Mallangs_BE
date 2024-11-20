//package com.mallangs.domain.member;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity(name = "member_role")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class MemberRole {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//}