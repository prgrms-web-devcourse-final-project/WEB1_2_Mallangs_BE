package com.mallangs.domain.oauth2.response;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {
        /**
         * 리소스로 부터 전달받는 최초 데이터 받기 위한 객체
         */
    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getNickname() {
        return attribute.get("name").toString();
    }
}
