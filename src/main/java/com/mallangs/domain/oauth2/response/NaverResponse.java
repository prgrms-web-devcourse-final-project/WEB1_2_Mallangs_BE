package com.mallangs.domain.oauth2.response;

import lombok.RequiredArgsConstructor;

import java.util.Map;
        /**
        json 데이터가 다른 리소스 서버와 다르게
         중첩되어 있어서 구현이 좀 더 복잡하다.
        {
          "resultcode": "00",
          "message": "success",
          "response": {
            "id": "1234567890",
            "email": "user@example.com",
            "name": "홍길동",
            "profile_image": "https://example.com/profile.jpg"
          }
        }
         **/
@RequiredArgsConstructor
public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = getResponse();
        Object id = response.get("id");
        return id != null ? id.toString() : "";
    }
    @Override
    public String getEmail() {
        Map<String, Object> response = getResponse();
        Object email = response.get("email");
        return email != null ? email.toString() : "";
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = getResponse();
        Object name = response.get("name");
        return name != null ? name.toString() : "";
    }
    /**
     response 객체를 속성(attributes)에서 안전하게 추출하는 헬퍼 메서드.
     만약 response 가 유효한 Map 이 아니라면 IllegalArgument Exception 을 발생시킵니다.
     */
    private Map<String, Object> getResponse() {
        Object response = attribute.get("response");
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        }
        throw new IllegalArgumentException("Invalid response structure: 'response' field is missing or not a Map.");
    }
}
