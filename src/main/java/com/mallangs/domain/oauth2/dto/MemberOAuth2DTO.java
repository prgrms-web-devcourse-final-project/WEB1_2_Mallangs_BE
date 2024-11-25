package com.mallangs.domain.oauth2.dto;

import lombok.Getter;

@Getter
public class MemberOAuth2DTO {

    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String role;

}
