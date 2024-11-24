package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberGetResponse {

    private String userId;
    private String nickname;
    private String email;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MemberAddressResponse> addresses;

    public MemberGetResponse(Member member) {
        this.userId = member.getUserId().getValue();
        this.nickname = member.getNickname().getValue();
        this.email = member.getEmail().getValue();
        this.profileImage = member.getProfileImage();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        if (!member.getAddresses().isEmpty()){
            this.addresses = member.getAddresses().stream()
                    .map(MemberAddressResponse::new)
                    .collect(Collectors.toList());
        }else {
            this.addresses = new ArrayList<>();
        }

    }
}
