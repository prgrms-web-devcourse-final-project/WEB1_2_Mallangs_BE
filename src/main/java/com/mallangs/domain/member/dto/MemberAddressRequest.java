package com.mallangs.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberAddressRequest {
    @NotNull(message = "주소ID는 필수입니다.")
    private Long addressId;
}
