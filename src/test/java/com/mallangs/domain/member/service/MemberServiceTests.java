package com.mallangs.domain.member.service;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("[service] 회원 서비스 단위 테스트")
public class MemberServiceTests {

    @InjectMocks
    private MemberUserService memberUserService;

    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;


    @DisplayName("회원가입")
    @Test
    void createMember() {
        //given
        Member member = Member.builder().userId(new UserId("testUserId")).password(new Password("testPassword1!!", passwordEncoder)).email(new Email("test123@test.com")).nickname(new Nickname("testNickname")).hasPet(true).build();

    }

}
