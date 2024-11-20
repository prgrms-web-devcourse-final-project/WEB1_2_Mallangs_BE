package com.mallangs.domain.member;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
