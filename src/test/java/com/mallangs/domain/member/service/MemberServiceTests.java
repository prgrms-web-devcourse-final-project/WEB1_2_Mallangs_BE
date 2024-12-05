package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.MemberCreateRequest;
import com.mallangs.domain.member.dto.MemberRegisterRequest;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-dev.properties")
public class MemberServiceTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberUserService memberUserService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createMember() {

    }

    @Test
    void createCreateRequest() {
        //given
        MemberCreateRequest requestData = createData();

        //when
//        MemberRegisterRequest memberRegisterRequest = memberUserService.create(requestData);
//
//        then
//        Assertions.assertEquals(memberRegisterRequest.getUserId(), "testId1223");
    }


    MemberCreateRequest createData() {
        return MemberCreateRequest.builder().userId("test1234213").password("P@ss0rd1223")
                .email("test.email2213024@provider.net").nickname("testname2").hasPet(true).build();
    }

}
