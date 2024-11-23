package com.mallangs.repository;

import com.mallangs.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberRepositoryTests {

    MemberRepository memberRepository;

    @Test
    public void findByUserIdTest() {
        //given

        //when
        memberRepository.findByUserId("userId");

        //then

    }
}
