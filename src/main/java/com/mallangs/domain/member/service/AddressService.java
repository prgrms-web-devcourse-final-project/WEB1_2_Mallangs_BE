package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.MemberAddressRequest;
import com.mallangs.domain.member.dto.MemberAddressResponse;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    //주소 추가
    public void create(String email, MemberAddressRequest memberAddressRequest) {
        try {
            Address address = memberAddressRequest.toEntity();

            Member foundMember = memberRepository.findByEmail(new Email(email))
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
            foundMember.getAddresses().add(address);

            memberRepository.save(foundMember);
        } catch (Exception e) {
            log.error("주소 추가에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //주소 확인
    public List<MemberAddressResponse> get(Long memberId) {
        try {
            List<Address> addresses = addressRepository.findByMemberId(memberId);
            return addresses.stream().map(MemberAddressResponse::new).toList();
        } catch (Exception e) {
            log.error("주소 조회에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

    //주소 삭제
    public void delete(String email, Long addressId) {
        try {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.ADDRESS_NOT_FOUND));
            Member foundMember = memberRepository.findByEmail(new Email(email))
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

            foundMember.removeAddress(address);
        } catch (Exception e) {
            log.error("주소 삭제에 실패하였습니다. {}", e.getMessage());
            throw e;
        }
    }

}
