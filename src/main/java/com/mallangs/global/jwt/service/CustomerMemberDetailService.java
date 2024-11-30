package com.mallangs.global.jwt.service;

import com.mallangs.global.jwt.entity.CustomMemberDetails;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomerMemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("userId: {}",userId);
        Member member = memberRepository.findByUserId(new UserId(userId))
                .orElseThrow(()-> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (member != null) {
            return new CustomMemberDetails(member);
        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
    }
}
