package com.mallangs.domain.member.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AddressCreateSuccessResponse {
    private String success;
    private String message;
    private String userId;

    public AddressCreateSuccessResponse(String userId) {
        this.success = "성공";
        this.message = "주소 등록에 성공하였습니다.";
        this.userId = userId;
    }
}
