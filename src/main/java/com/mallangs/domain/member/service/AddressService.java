package com.mallangs.domain.member.service;

import com.mallangs.domain.member.dto.AddressCreateSuccessResponse;
import com.mallangs.domain.member.dto.AddressDeleteSuccessResponse;
import com.mallangs.domain.member.dto.MemberAddressRequest;
import com.mallangs.domain.member.dto.MemberAddressResponse;
import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    //주소 추가
    public AddressCreateSuccessResponse create(String userId, MemberAddressRequest memberAddressRequest) {
        Member foundMember = memberRepository.findByUserId(new UserId(userId))
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        try {
            Address address = memberAddressRequest.toEntity();
            address.addMember(foundMember);
            addressRepository.save(address);

            foundMember.getAddresses().add(address);
            memberRepository.save(foundMember);
            return new AddressCreateSuccessResponse(foundMember.getUserId().getValue());
        } catch (Exception e) {
            log.error("주소 추가에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //주소 확인
    public List<MemberAddressResponse> get(Long memberId) {
        try {
            List<Address> addresses = addressRepository.findByMemberId(memberId);
            return addresses.stream().map(MemberAddressResponse::new).toList();
        } catch (Exception e) {
            log.error("주소 조회에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

    //주소 삭제
    public AddressDeleteSuccessResponse delete(Long addressId) {
        try {
            Optional<Address> foundAddress = addressRepository.findById(addressId);
            if (foundAddress.isEmpty()) {
                throw new MallangsCustomException(ErrorCode.ADDRESS_NOT_FOUND);
            }
            addressRepository.deleteById(addressId);
            return new AddressDeleteSuccessResponse(addressId);
        } catch (Exception e) {
            log.error("주소 삭제에 실패하였습니다. {}", e.getMessage());
            throw new MallangsCustomException(ErrorCode.FAILURE_REQUEST);
        }
    }

}
