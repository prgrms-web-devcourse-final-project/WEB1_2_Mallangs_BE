package com.mallangs.domain.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class IsRead {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isReadId;

    private boolean read;
}
