//package com.mallangs.domain.member;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Entity(name = "role")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "role_name")
//    private String roleName;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @Column(name = "member_role_list")
//    private List<MemberRole> memberRoleList;
//}