package com.mallangs.domain.member.controller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokensRequest {
    private String refreshToken;
}
