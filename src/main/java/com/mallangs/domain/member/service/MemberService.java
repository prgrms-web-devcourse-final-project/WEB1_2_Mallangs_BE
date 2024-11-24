package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.MemberAddressResponse;
import com.mallangs.domain.member.dto.MemberGetResponse;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberGetResponse> getMemberList(){
        List<Member> members = memberRepository.memberList();
        return members.stream().map(MemberGetResponse::new)
                .collect(Collectors.toList());
    }
}
