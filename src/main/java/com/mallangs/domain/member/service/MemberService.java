package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.MemberCreateRequest;
import com.mallangs.domain.member.dto.MemberGetResponse;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    public String create(MemberCreateRequest memberCreateRequest) {
        try {
            Member member = memberCreateRequest.toEntity();
            memberRepository.save(member);
            return member.getUserId().getValue();
        }catch(Exception e){
            log.info("회원가입에 실패하였습니다. {}",e.getMessage());
            throw e;
        }
    }

    //회원조회
    public MemberGetResponse get(Long memberId) {
        try {
            //회원예외넣기
            Member foundMember = memberRepository.findById(memberId).orElseThrow();
            return new MemberGetResponse(foundMember);

        }catch(Exception e){
            log.info("회원조회에 실패하였습니다. {}",e.getMessage());
            throw e;
        }
    }

    //회원 리스트 조회
    public List<MemberGetResponse> getMemberList(){
        List<Member> members = memberRepository.memberList();
        return members.stream().map(MemberGetResponse::new)
                .collect(Collectors.toList());
    }
}
