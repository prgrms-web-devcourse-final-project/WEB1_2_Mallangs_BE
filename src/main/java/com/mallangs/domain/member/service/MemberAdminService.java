package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.MemberGetResponse;
import com.mallangs.domain.member.dto.PageRequestDTO;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberAdminService {

    private final MemberRepository memberRepository;

    //회원삭제
    public void unActive(Long memberId) {
        try {
            Member foundMember = memberRepository.findById(memberId).orElseThrow(()
                    -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
            memberRepository.delete(foundMember);

        } catch (Exception e) {
            log.error("회원삭제에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //회원 검색 - UserId
    public List<MemberGetResponse> getMemberByUserId(String userIdValue) {
        try {
            List<Member> members = memberRepository.searchMemberByUserId(userIdValue);
            return members.stream().map(MemberGetResponse::new).toList();
        } catch (Exception e) {
            log.error("UserId로 회원검색에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //회원 검색 - Email
    public List<MemberGetResponse> getMemberByEmail(String EmailValue) {
        try {
            List<Member> members = memberRepository.searchMemberByEmail(EmailValue);
            return members.stream().map(MemberGetResponse::new).toList();
        } catch (Exception e) {
            log.error("Email로 회원검색에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //회원 검색 - Nickname
    public List<MemberGetResponse> getMemberByNickname(String NicknameValue) {
        try {
            List<Member> members = memberRepository.searchMemberByNickname(NicknameValue);
            return members.stream().map(MemberGetResponse::new).toList();
        } catch (Exception e) {
            log.error("Nickname로 회원검색에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //회원 리스트 조회, 주소포함
    public Page<MemberGetResponse> getMemberList(PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("nickname").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            List<Member> members = memberRepository.memberList();
            List<MemberGetResponse> memberList = members.stream().map(MemberGetResponse::new)
                    .toList();

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), memberList.size());
            List<MemberGetResponse> memberPage = memberList.subList(start, end);

            return new PageImpl<>(memberPage, pageable, memberList.size());
        } catch (Exception e) {
            log.error("회원리스트 조회에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

}
