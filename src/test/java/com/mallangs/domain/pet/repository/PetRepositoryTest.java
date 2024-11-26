package com.mallangs.domain.pet.repository;


import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PetRepositoryTest {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Member testMember1;
    private Member testMember2;
    private Address testAddress1;
    private Address testAddress2;
    private Pet testPet1;
    private Pet testPet2;

    @BeforeEach
    void setUp(){
        testAddress1 = Address.builder()
                .addressName("서울테스트")
                .x(127.0276)
                .y(37.4979)
                .region1depthName("서울")
                .region2depthName("강남구")
                .region3depthName("역삼동")
                .build();
        addressRepository.save(testAddress1);

        testAddress2 = Address.builder()
                .addressName("울산테스트") // 추가
                .x(129.3280)
                .y(35.5376)
                .region1depthName("울산")
                .region2depthName("남구")
                .region3depthName("신정동")
                .build();
        addressRepository.save(testAddress2);

        testMember1 = Member.builder()
                .address1(testAddress1)
                .build();
        testMember2 = Member.builder()
                .address1(testAddress2)
                .build();
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);

        for(int i =0; i<10;i++) {
            testPet1 = Pet.builder()
                    .member(testMember1)
                    .isOpenProfile(i % 2 != 0) // 홀수일 때 true, 짝수일 때 false
                    .isActive(true)
                    .name("멍멍이"+i)
                    .weight(157D+i)
                    .gender(PetGender.MALE)
                    .petType(PetType.DOG)
                    .isNeutering(true)
                    .build();

            testPet2 = Pet.builder()
                    .member(testMember2)
                    .isOpenProfile(i % 2 != 0) // 홀수일 때 true, 짝수일 때 false
                    .isActive(true)
                    .name("냥냥이"+i)
                    .weight(131D+i)
                    .gender(PetGender.MALE)
                    .petType(PetType.CAT)
                    .isNeutering(true)
                    .build();
            petRepository.save(testPet1);
            petRepository.save(testPet2);
        }
}

    @Test
    @DisplayName("본인의 반려동물 목록 조회")
    void findAllPetsByMemberId() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<Pet> result = petRepository.findAllPetsByMemberId(testMember1.getMemberId(),pageable);
        Page<Pet> result2 = petRepository.findAllPetsByMemberId(testMember2.getMemberId(),pageable);
        //then

        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result2.getTotalElements()).isEqualTo(10);

        int index = 0;
        for (Pet pet : result) {
            assertThat(pet.getName()).isEqualTo("멍멍이" + index);
            index++;
        }
        index = 0;
        for (Pet pet : result2) {
            assertThat(pet.getName()).isEqualTo("냥냥이" + index);
            index++;
        }
    }

    @Test
    @DisplayName("공개 프로필 반려동물 전체 조회")
    void findAllOpenProfilePets() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<Pet> result = petRepository.findAllOpenProfilePets(pageable);
        //then
        assertThat(result.getTotalElements()).isEqualTo(10);

        for (Pet pet : result) {
            assertThat(pet.getIsOpenProfile()).isTrue();
        }
    }

    @Test
    @DisplayName("반경 내 주변 반려동물 조회")
    void findNearbyPets() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        Double centerY = 37.4977;
        Double centerX = 127.0256;
        Double radiusInKm = 10.0;

//        Double centerY = 35.5377;
//        Double centerX = 129.3276;
//        Double radiusInKm = 10.0;

        //when
        Page<Pet> result = petRepository.findNearbyPets(centerY, centerX, radiusInKm, null, null, null, pageable);

        //then
        assertThat(result).isNotEmpty();
        // 첫 번째 Pet 객체의 지역명 검증
        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion1depthName()).isEqualTo("서울");
        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion2depthName()).isEqualTo("강남구");
        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion3depthName()).isEqualTo("역삼동");


//        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion1depthName()).isEqualTo("울산");
//        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion2depthName()).isEqualTo("남구");
//        assertThat(result.getContent().get(0).getMember().getAddress1().getRegion3depthName()).isEqualTo("신정동");
    }
}