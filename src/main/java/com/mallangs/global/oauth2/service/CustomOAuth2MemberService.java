package com.mallangs.global.oauth2.service;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.global.oauth2.response.GoogleResponse;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.util.PasswordGenerator;
import com.mallangs.global.oauth2.dto.CustomOAuth2Member;
import com.mallangs.global.oauth2.dto.MemberOAuth2DTO;
import com.mallangs.global.oauth2.response.NaverResponse;
import com.mallangs.global.oauth2.response.OAuth2Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


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
        String userId = generateUserId((oAuth2Response.getProvider() + oAuth2Response.getProviderId()+" ").trim());
        String nickname = deleteSpace(oAuth2Response.getNickname().trim());
        String email = oAuth2Response.getEmail();
        Optional<Member> existMember = memberRepository.findByUserId(new UserId(userId));

        log.info("nickName {}",nickname);
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
            memberDTO.changeMemberId(member.getMemberId());
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
            memberDTO.changeMemberId(member.getMemberId());
            memberDTO.changeUserId(member.getUserId().getValue());
            memberDTO.changePassword(member.getPassword().getValue());
            memberDTO.changeNickname(member.getNickname().getValue());
            memberDTO.changeEmail(member.getEmail().getValue());
            memberDTO.changeRole(member.getMemberRole().name());

            return new CustomOAuth2Member(memberDTO);
        }
    }
    //userId 만들기
    private String generateUserId(String userId) {
        // 1. 정규 표현식에 맞는 문자만 남기기
        log.info("userId : {}",userId);
        String baseUserId = userId.replaceAll("[^ㄱ-ㅎ가-힣a-zA-Z0-9-_\\s]", "");

        // 3. 길이 조정: 6자 이상 12자 이하
        if (baseUserId.length() > 12) {
            baseUserId = baseUserId.substring(0, 12);
        }

        log.info("baseUserId : {}",baseUserId);
        if (baseUserId.length() < 6) {
            baseUserId = String.format("%-6s", baseUserId).replace(' ', '_');
        }

        log.info("baseUserId : {}",baseUserId);
        // 4. 유효성 검사
        if (!baseUserId.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9-_\\s]{6,12}$")) {
            throw new IllegalArgumentException("Generated userId does not match the required pattern: " + baseUserId);
        }

        // 5. 유일성 확인 및 조정 (이미 존재하는 userId인 경우)
        int suffix = 1;
        String uniqueUserId = baseUserId;
        while (memberRepository.existsByUserId(new UserId(uniqueUserId))) {
            String tempSuffix = "_" + suffix;
            if (baseUserId.length() + tempSuffix.length() > 12) {
                uniqueUserId = baseUserId.substring(0, 12 - tempSuffix.length()) + tempSuffix;
            } else {
                uniqueUserId = baseUserId + tempSuffix;
            }
            suffix++;
            if (suffix > 100) { // 무한 루프 방지를 위해 최대 시도 횟수 설정
                throw new IllegalArgumentException("Unable to generate unique userId");
            }
        }
        log.info("uniqueUserId : {}",uniqueUserId);

        return uniqueUserId;
    }
    private String deleteSpace(String name){
        if (name == null){
            name = UUID.randomUUID().toString().substring(1,14);
        }
        String ChangedName = name.replaceAll("[!#%&'*-+,$^*()./:;<=>?@_`}~]", "");
        if (ChangedName.length() > 15 || ChangedName.length() < 2){
            return UUID.randomUUID().toString().substring(1,14);
        }
        return ChangedName.replaceAll(" ", "");
    }
}
