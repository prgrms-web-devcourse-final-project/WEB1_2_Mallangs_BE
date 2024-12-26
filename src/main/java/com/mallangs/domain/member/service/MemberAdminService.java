package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberAdminService {

    private final MemberRepository memberRepository;

    //회원 차단
    public int banMember(MemberBanRequest memberBanRequest) {
        try {
            //차단 기간
            LocalDateTime expiryDate = LocalDateTime.now().plusDays(memberBanRequest.getDays());
            return memberRepository.blockMembersForExpiryDate(expiryDate, memberBanRequest.getMemberIds(), memberBanRequest.getReason());
        } catch (Exception e) {
            log.error("회원차단에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //회원 활성화
    public void activeMembers() {
        try {
            memberRepository.activeBannedMembers(LocalDateTime.now());
        } catch (Exception e) {
            log.error("회원 차단기간 만료 후 자동 차단해제에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //회원 리스트 검색 - UserId
    public Page<MemberGetResponseOnlyMember> getMemberListByUserId(
            MemberGetRequestByUserId memberGetRequestByUserId, PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("nickname").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            //날짜 전처리 7, 15, 30일 이전으로
            LocalDateTime createAt = LocalDateTime.now().minusDays(memberGetRequestByUserId.getDays());
            Boolean isActive = memberGetRequestByUserId.getIsActive();
            String userId = memberGetRequestByUserId.getUserId();

            return memberRepository.memberListByUserId(isActive, new UserId(userId), createAt, pageable);
        } catch (Exception e) {
            log.error("회원리스트 조회에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //회원 리스트 검색 - Email
    public Page<MemberGetResponseOnlyMember> getMemberListByEmail(
            MemberGetRequestByEmail memberGetRequestByEmail, PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("nickname").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            //날짜 전처리 7, 15, 30일 이전으로
            LocalDateTime createAt = LocalDateTime.now().minusDays(memberGetRequestByEmail.getDays());
            Boolean isActive = memberGetRequestByEmail.getIsActive();
            String email = memberGetRequestByEmail.getEmail();

            return memberRepository.memberListByEmail(isActive, new Email(email), createAt, pageable);
        } catch (Exception e) {
            log.error("회원리스트 조회에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //회원 리스트 검색 - Nickname
    public Page<MemberGetResponseOnlyMember> getMemberListByNickname(
            MemberGetRequestByNickname memberGetRequestByNickname, PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("nickname").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            //날짜 전처리 7, 15, 30일 이전으로
            LocalDateTime createAt = LocalDateTime.now().minusDays(memberGetRequestByNickname.getDays());
            Boolean isActive = memberGetRequestByNickname.getIsActive();
            String nickname = memberGetRequestByNickname.getNickname();

            return memberRepository.memberListByNickname(isActive, nickname, createAt, pageable);
        } catch (Exception e) {
            log.error("회원리스트 조회에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }
}
