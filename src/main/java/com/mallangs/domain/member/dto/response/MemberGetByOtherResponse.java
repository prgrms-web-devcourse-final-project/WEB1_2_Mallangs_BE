package com.mallangs.domain.member.dto.response;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.dto.PetMemberProfileResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberGetByOtherResponse {

    private Long memberId;
    private String userId;
    private String nickname;
    private String email;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PetMemberProfileResponse> pets;

    public MemberGetByOtherResponse(Member member) {
        this.memberId = member.getMemberId();
        this.userId = member.getUserId().getValue();
        this.nickname = member.getNickname().getValue();
        this.email = member.getEmail().getValue();
        this.profileImage = member.getProfileImage();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();

        if (!member.getPets().isEmpty()) {
            this.pets = member.getPets().stream()
                    .map(PetMemberProfileResponse::new)
                    .collect(Collectors.toList());
        } else {
            this.pets = new ArrayList<>();
        }

    }
}
