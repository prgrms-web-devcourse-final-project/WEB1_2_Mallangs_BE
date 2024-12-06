package com.mallangs.domain.member.dto;

import com.mallangs.domain.member.entity.Member;

import com.mallangs.domain.pet.dto.PetMemberProfileResponse;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import lombok.Getter;
import lombok.ToString;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class MemberGetResponse {

    private Long memberId;
    private String userId;
    private String nickname;
    private String email;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MemberAddressResponse> addresses;

    private List<PetMemberProfileResponse> pets;

    public MemberGetResponse(Member member) {
        this.memberId = member.getMemberId();
        this.userId = member.getUserId().getValue();
        this.nickname = member.getNickname().getValue();
        this.email = member.getEmail().getValue();
        this.profileImage = member.getProfileImage();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
        if (!member.getAddresses().isEmpty()) {
            this.addresses = member.getAddresses().stream()
                    .map(MemberAddressResponse::new)
                    .collect(Collectors.toList());
        } else {
            this.addresses = new ArrayList<>();
        }
        if (!member.getPets().isEmpty()) {
            this.pets = member.getPets().stream()
                    .map(PetMemberProfileResponse::new)
                    .collect(Collectors.toList());
        } else {
            this.pets = new ArrayList<>();
        }

    }
}
