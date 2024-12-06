package com.mallangs.global.jwt.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokensRequest {
    private String refreshToken;
}
