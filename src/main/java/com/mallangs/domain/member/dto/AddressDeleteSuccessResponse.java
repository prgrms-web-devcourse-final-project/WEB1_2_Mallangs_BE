package com.mallangs.domain.member.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AddressDeleteSuccessResponse {
    private String success;
    private String message;
    private Long addressId;

    public AddressDeleteSuccessResponse(Long addressId) {
        this.success = "성공";
        this.message = "주소 삭제에 성공하였습니다.";
        this.addressId = addressId;
    }
}
