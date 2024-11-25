package com.mallangs.domain.oauth2.service;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.util.PasswordGenerator;
import com.mallangs.domain.oauth2.dto.CustomOAuth2Member;
import com.mallangs.domain.oauth2.dto.MemberOAuth2DTO;
import com.mallangs.domain.oauth2.response.GoogleResponse;
import com.mallangs.domain.oauth2.response.NaverResponse;
import com.mallangs.domain.oauth2.response.OAuth2Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {
    /**
      * 리소스 서버로 부터 값을 받아와서
      * 어플리케이션에서 정제하는 로직
   */
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //어떤 리소스 서버인지 확인
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        //중복많을 시 UUID 설정 + 닉네임 길이 늘이기
        String userId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String shortenUserId = userId.substring(6,12);
        String nickname = oAuth2Response.getNickname().trim().substring(2,15);
        String email = oAuth2Response.getEmail();
        Optional<Member> existMember = memberRepository.findByUserId(new UserId(shortenUserId));

        // 회원이 존재하지 않으면 자동 회원가입
        if (existMember.isEmpty()) {
            String password = PasswordGenerator.generatePassword();
            Member member = Member.builder()
                    .userId(new UserId(userId))
                    .password(new Password(password, passwordEncoder))
                    .nickname(new Nickname(nickname))
                    .email(new Email(email))
                    .hasPet(false).build();
            memberRepository.save(member);

            //OAuth2 인증 위한 데이터 입력
            MemberOAuth2DTO memberDTO = new MemberOAuth2DTO();
            memberDTO.changeUserId(member.getUserId().getValue());
            memberDTO.changePassword(member.getPassword().getValue());
            memberDTO.changeNickname(member.getNickname().getValue());
            memberDTO.changeEmail(member.getEmail().getValue());
            memberDTO.changeRole(member.getMemberRole().name());

            return new CustomOAuth2Member(memberDTO);
        } else {
            // 회원이 존재한다면, 비밀번호 업데이트 후 로그인.
            String password = PasswordGenerator.generatePassword();
            Member member = existMember.get();
            member.changePassword(new Password(password, passwordEncoder));

            memberRepository.save(member);

            //OAuth2 인증 위한 데이터 입력
            MemberOAuth2DTO memberDTO = new MemberOAuth2DTO();
            memberDTO.changeUserId(member.getUserId().getValue());
            memberDTO.changePassword(member.getPassword().getValue());
            memberDTO.changeNickname(member.getNickname().getValue());
            memberDTO.changeEmail(member.getEmail().getValue());
            memberDTO.changeRole(member.getMemberRole().name());

            return new CustomOAuth2Member(memberDTO);
        }
    }
}
