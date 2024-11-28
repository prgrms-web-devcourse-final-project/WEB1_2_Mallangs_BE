package com.mallangs.global.oauth2.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {
        /**
         * 일반 로그인의 UserDetails 와 같은 역할
         * OAuth2 에서는 OAuth2User 사용
         */
    private final MemberOAuth2DTO memberOAuth2DTO;

    @Override
    //소셜마다 다른 json 데이터를 제공함으로 null 처리
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((GrantedAuthority) memberOAuth2DTO::getRole);
        return collection;
    }

    @Override
    public String getName() {
        return memberOAuth2DTO.getUserId();
    }

    public String getUsername() {
        return memberOAuth2DTO.getNickname();
    }

    public String getEmail() {
        return memberOAuth2DTO.getEmail();
    }

    public String getRole(){
        return memberOAuth2DTO.getRole();
    }
}
