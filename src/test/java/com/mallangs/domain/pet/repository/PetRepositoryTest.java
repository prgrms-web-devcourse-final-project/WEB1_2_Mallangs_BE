package com.mallangs.domain.pet.repository;


import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class PetRepositoryTest {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member testMember1;
    private Member testMember2;
    private Address testAddress1;
    private Address testAddress2;
    private Pet testPet1;
    private Pet testPet2;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp(){

        testMember1 = Member.builder()
                .userId(new UserId("testId1234"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
                .build();
        memberRepository.save(testMember1);

        testMember2 = Member.builder()
                .userId(new UserId("asaaas11"))
                .password(new Password("11asdasAsds!!", passwordEncoder))
                .email(new Email("test1@test.com"))
                .nickname(new Nickname("12"))
                .hasPet(true)
                .build();
        memberRepository.save(testMember2);

        Point point1 = geometryFactory.createPoint(new Coordinate(127.0276, 37.4979));
        point1.setSRID(4326);
        testAddress1 = Address.builder()
                .addressName("서울테스트")
                .addressType("테스트")
                .point(point1)
                .region1depthName("서울")
                .region2depthName("강남구")
                .region3depthName("역삼동")
                .member(testMember1)  // Member 설정
                .build();
        addressRepository.save(testAddress1);

        Point point2 = geometryFactory.createPoint(new Coordinate(129.3280, 35.5376));
        point2.setSRID(4326);
        testAddress2 = Address.builder()
                .addressName("울산테스트")
                .addressType("테스트")
                .point(point2)
                .region1depthName("울산")
                .region2depthName("남구")
                .region3depthName("신정동")
                .member(testMember2)  // Member 설정
                .build();
        addressRepository.save(testAddress2);

        testMember1.addAddress(testAddress1);
        testMember2.addAddress(testAddress2);

        for(int i =0; i<10;i++) {
            testPet1 = Pet.builder()
                    .member(testMember1)
                    .isOpenProfile(i % 2 != 0) // 홀수일 때 true, 짝수일 때 false
                    .isActive(true)
                    .name("멍멍이"+i)
                    .weight(157D+i)
                    .gender(PetGender.MALE)
                    .petType(PetType.DOG)
                    .isRepresentative(i==9)
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
                    .isRepresentative(i==0)
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
//        Double centerY = 37.4979; //서울 위도
//        Double centerX = 127.0276;
//        Double radiusInKm = 20.0;

        Double centerY = 35.5377; //울산
        Double centerX = 129.3280;
        Double radiusInKm = 20.0;

        //when
        Page<Pet> result = petRepository.findNearbyPets(centerY, centerX, radiusInKm, null, null, null, pageable);

        //then
        assertThat(result).isNotEmpty();
        //첫번쨰 반려동물
        Pet firstPet = result.getContent().get(0);
        assertThat(firstPet.getMember()).isNotNull();

        List<Address> addresses = firstPet.getMember().getAddresses();
        assertThat(addresses).isNotEmpty(); // 주소가 있는지 확인
        Address address = addresses.get(0);


        // 첫 번째 Pet 객체의 지역명 검증
//        assertThat(address.getRegion1depthName()).isEqualTo("서울");
//        assertThat(address.getRegion2depthName()).isEqualTo("강남구");
//        assertThat(address.getRegion3depthName()).isEqualTo("역삼동");

        assertThat(address.getRegion1depthName()).isEqualTo("울산");
        assertThat(address.getRegion2depthName()).isEqualTo("남구");
        assertThat(address.getRegion3depthName()).isEqualTo("신정동");
    }

    @Test
    @DisplayName("memberId로 펫 반려동물 여부 확인")
    void testExistsByMemberId() {
        //given
        Long memberId = testMember1.getMemberId();

        //when
        boolean exists = petRepository.existsByMemberId(memberId);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("대표 말랑이 조회")
    void testFindRepresentativePetByMemberId() {
        //given
        // 기존 더미 데이터 중 강아지는 마지막, 고양이는 첫번째

        //when
        Optional<Pet> foundPet = petRepository.findRepresentativePetByMemberId(testMember1.getMemberId());
        Optional<Pet> foundPet2 = petRepository.findRepresentativePetByMemberId(testMember2.getMemberId());

        //then
        assertThat(foundPet).isPresent();
        assertThat(foundPet2).isPresent();

        assertThat(foundPet.get().getIsRepresentative()).isTrue();
        assertThat(foundPet2.get().getIsRepresentative()).isTrue();

        assertThat(foundPet.get().getMember()).isEqualTo(testMember1);
        assertThat(foundPet2.get().getMember()).isEqualTo(testMember2);
    }
}