package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.*;
import com.mallangs.domain.member.dto.response.MemberGetByOtherResponse;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.member.util.PasswordGenerator;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    예외처리 수정 필요
    */
@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberUserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final AddressRepository addressRepository;

    @Value("${spring.mail.username}")
    private String from;

    //회원가입
    public MemberRegisterRequest create(MemberCreateRequest memberCreateRequest) {
        if (memberRepository.existsByUserId(new UserId(memberCreateRequest.getUserId()))) {
            throw new MallangsCustomException(ErrorCode.DUPLICATE_USER_ID);
        }
        if (memberRepository.existsByEmail(new Email(memberCreateRequest.getEmail()))) {
            throw new MallangsCustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        try {
            Member member = memberCreateRequest.toEntityMember();
            member.changePassword(new Password(memberCreateRequest.getPassword(), passwordEncoder));

            Address address = memberCreateRequest.toEntityAddress();
            address.addMember(member);

            member.addAddress(address);
            memberRepository.save(member);
            addressRepository.save(address);

            return new MemberRegisterRequest(member);
        } catch (Exception e) {
            log.error("회원가입 도중 DB 저장에 실패하였습니다.");
            throw new MallangsCustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    //회원조회 -memberId
    public MemberGetResponse get(Long memberId) {
        Member foundMember = memberRepository.findByUserIdForProfile(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (foundMember.getIsActive().equals(false)) {
            throw new MallangsCustomException(ErrorCode.BANNED_MEMBER);
        }
        return new MemberGetResponse(foundMember);
    }

    //회원조회(타인) -memberId
    public MemberGetByOtherResponse getByOther(Long memberId) {
        Member foundMember = memberRepository.findByUserIdForProfile(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (foundMember.getIsActive().equals(false)) {
            throw new MallangsCustomException(ErrorCode.BANNED_MEMBER);
        }

        return new MemberGetByOtherResponse(foundMember);
    }

    //회원조회 -userId
    public MemberGetResponse get(String userId) {
        Member foundMember = memberRepository.findByUserIdForProfile(new UserId(userId))
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (foundMember.getIsActive().equals(false)) {
            throw new MallangsCustomException(ErrorCode.BANNED_MEMBER);
        }
        return new MemberGetResponse(foundMember);
    }

    //회원조회(타인) -userId
    public MemberGetByOtherResponse getByOther(String userId) {
        Member foundMember = memberRepository.findByUserIdForProfile(new UserId(userId))
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (foundMember.getIsActive().equals(false)) {
            throw new MallangsCustomException(ErrorCode.BANNED_MEMBER);
        }

        return new MemberGetByOtherResponse(foundMember);
    }

    //회원정보 수정
    public MemberGetResponse update(MemberUpdateRequest memberUpdateRequest, Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        try {

            //회원 정보 수정
            foundMember.change(
                    memberUpdateRequest.getNickname(),
                    memberUpdateRequest.getPassword(),
                    memberUpdateRequest.getEmail(),
                    passwordEncoder
            );
            Member save = memberRepository.save(foundMember);
            return new MemberGetResponse(save);
        } catch (Exception e) {
            log.error("회원수정 도중 서버에 에러가 발생했습니다.");
            throw new MallangsCustomException(ErrorCode.FAILURE);
        }
    }

    //회원 탈퇴
    public void delete(Long memberId) {
        try {
            Member foundMember = memberRepository.findByMemberId(memberId).orElseThrow(()
                    -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
            foundMember.changeIsActive(false);
            memberRepository.save(foundMember);
        } catch (Exception e) {
            log.error("회원비활성화에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //회원 아이디 찾기
    public String findUserId(MemberFindUserIdRequest memberFindUserIdRequest) {
        Email email = new Email(memberFindUserIdRequest.getEmail());
        Nickname nickname = new Nickname(memberFindUserIdRequest.getNickname());

        Member foundMember = memberRepository.findByEmailAndNickname(email, nickname)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        return foundMember.getUserId().getValue();
    }

    //비밀번호 확인
    public MemberCheckPasswordResponse checkPassword(PasswordDTO passwordDTO, String userId) {
        Member foundMember = memberRepository.findByUserId(new UserId(userId)).orElseThrow(
                () -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        //패스워드 인코더 매치 사용해서 해시 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(passwordDTO.getPassword(), foundMember.getPassword().getValue())) {
            throw new MallangsCustomException(ErrorCode.UNMATCHED_PASSWORD);
        }
        return new MemberCheckPasswordResponse(foundMember);
    }

    //회원 비밀번호 찾기 -> 임시비밀번호 변경, 메일 메세지 생성
    public MemberSendMailResponse findPassword(MemberFindPasswordRequest memberFindPasswordRequest) {
            Email email = new Email(memberFindPasswordRequest.getEmail());
            UserId userId = new UserId(memberFindPasswordRequest.getUserId());

            //입력된 정보로 회원 확인
            Member foundMember = memberRepository.findByEmailAndUserId(email, userId)
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        try {
            log.info("회우너정보{}",foundMember);
            //임시비밀번호 만들기
            String tempPassword = PasswordGenerator.generatePassword();

            //회원 비밀번호 변경
            foundMember.changePassword(new Password(tempPassword, passwordEncoder));
            memberRepository.save(foundMember);

            //전송할 메세지 쓰기
            return writeMessage(email.getValue(), tempPassword);
        } catch (Exception e) {
            log.error("회원 비밀번호 찾기에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.DATABASE_ERROR);
        }
    }

    //ip주소 검증 -> 경고메일 메세지 생성
    public MemberSendMailResponse writeWarningMessage(String email) {

        // 메시지 작성 (HTML 형식 권장)
        String message = "안녕하세요. Mallang에 새로운 지역에서 접속이 발견되었습니다.\n"
                + "본인이 아니라면 비밀번호를 변경해주세요.\n"
                + "자세한 문의는 000-0000-0000으로 연락주세요.";

        MemberSendMailResponse dto = new MemberSendMailResponse();
        dto.setEmail(email);
        dto.setTitle("Mallang 새로운 지역 로그인 안내 이메일입니다.");
        dto.setMessage(message);
        return dto;
    }

    //이메일에 전송될 비밀번호 변경 메세지 작성
    public MemberSendMailResponse writeMessage(String email, String tempPassword) {
        MemberSendMailResponse dto = new MemberSendMailResponse();
        dto.setEmail(email);
        dto.setTitle("Mallang 임시비밀번호 안내 이메일입니다.");
        dto.setMessage("안녕하세요. Mallang 임시비밀번호 안내 관련 이메일 입니다.\n" + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다.\n" + "로그인 후에는 비밀번호 변경 바랍니다.\n"
                + "언제나 저희 서비스를 이용해 주셔서 감사합니다.");
        return dto;
    }

    //메일 전송
    public MemberSendMailResponse mailSend(MemberSendMailResponse memberSendMailResponse) throws MessagingException {
        try {
            //MimeMessageHelper 로 message + multi file 전송
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            //송수신자, 메세지 내용 등록
            helper.setFrom(from);
            helper.setTo(memberSendMailResponse.getEmail());
            helper.setSubject(memberSendMailResponse.getTitle());
            helper.setText(memberSendMailResponse.getMessage());
            helper.setReplyTo(from);

            //파일등록
//            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            emailSender.send(message);
            return memberSendMailResponse;
        } catch (Exception e) {
            log.error("메세지 전송에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE);
        }
    }

}
