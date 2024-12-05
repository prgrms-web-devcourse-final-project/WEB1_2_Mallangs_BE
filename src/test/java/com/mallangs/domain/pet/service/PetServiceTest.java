package com.mallangs.domain.pet.service;

import com.mallangs.domain.member.entity.Address;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import com.mallangs.domain.member.repository.AddressRepository;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.dto.*;
import com.mallangs.domain.pet.dto.PageRequest;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.entity.PetGender;
import com.mallangs.domain.pet.entity.PetType;
import com.mallangs.domain.pet.repository.PetRepository;

import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.jwt.entity.CustomMemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
class PetServiceTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PetService petService;

    private Member testMember1, testMember2;
    private CustomMemberDetails customMemberDetails1, customMemberDetails2;
    private Pet testPet1, testPet2, testPet3, testPet4;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private Address testAddress1, testAddress2;


    @BeforeEach
    void setUp() {
        // 회원 설정
        testMember1 = Member.builder()
                .memberId(1L) // Mock 객체에서 사용할 ID 설정
                .userId(new UserId("testId1234"))
                .password(new Password("1234Aa1!!", passwordEncoder))
                .email(new Email("test123@test.com"))
                .nickname(new Nickname("testname2"))
                .hasPet(true)
                .addresses(new ArrayList<>()) // 주소 목록 초기화
                .build();

        testMember2 = Member.builder()
                .memberId(2L) // Mock 객체에서 사용할 ID 설정
                .userId(new UserId("asaaas11"))
                .password(new Password("11asdasAsds!!", passwordEncoder))
                .email(new Email("test1@test.com"))
                .nickname(new Nickname("12"))
                .hasPet(true)
                .addresses(new ArrayList<>()) // 주소 목록 초기화
                .build();

        // 주소 설정
        testAddress1 = Address.builder()
                .id(1L) // Mock 객체에서 사용할 ID 설정
                .addressName("서울테스트")
                .addressType("테스트")
                .point(geometryFactory.createPoint(new Coordinate(127.0276, 37.4979)))
                .region1depthName("서울")
                .region2depthName("강남구")
                .region3depthName("역삼동")
                // Member 객체를 직접 설정하지 않고, memberId만 설정
                .build();

        testAddress2 = Address.builder()
                .id(2L) // Mock 객체에서 사용할 ID 설정
                .addressName("울산테스트")
                .addressType("테스트")
                .point(geometryFactory.createPoint(new Coordinate(129.3280, 35.5376)))
                .region1depthName("울산")
                .region2depthName("남구")
                .region3depthName("신정동")
                // Member 객체를 직접 설정하지 않고, memberId만 설정
                .build();
        testPet1 = Pet.builder()
                .petId(1L)
                .name("멍멍이0")
                .petType(PetType.DOG)
                .gender(PetGender.MALE)
                .member(testMember1)
                .birthdate(LocalDate.of(2020,1,1))
                .weight(5.0)
                .description("활발한 강아지")
                .isOpenProfile(true)
                .isNeutering(true)
                .microChip("1234123412341234")
                .isActive(true)
                .isRepresentative(true)
                .build();

        testPet2 = Pet.builder()
                .petId(2L)
                .name("야옹이1")
                .petType(PetType.CAT)
                .gender(PetGender.FEMALE)
                .member(testMember1)
                .birthdate(LocalDate.of(2021,2,2))
                .weight(4.0)
                .description(" 고양이")
                .isOpenProfile(false)
                .isNeutering(false)
                .microChip("4321432143214321")
                .isActive(false)
                .isRepresentative(false)
                .build();
        testPet3 = Pet.builder()
                .petId(3L)
                .name("멍멍이2")
                .petType(PetType.DOG)
                .gender(PetGender.MALE)
                .member(testMember2)
                .birthdate(LocalDate.of(2022,3,3))
                .weight(6.0)
                .description(" 강아지2")
                .isOpenProfile(true)
                .isNeutering(true)
                .microChip("1212121212121212")
                .isActive(true)
                .isRepresentative(true)
                .build();

        testPet4 = Pet.builder()
                .petId(4L)
                .name("야옹이3")
                .petType(PetType.CAT)
                .gender(PetGender.FEMALE)
                .member(testMember2)
                .birthdate(LocalDate.of(2023,4,4))
                .weight(3.0)
                .description("비공개 비활성 고양이")
                .isOpenProfile(false)
                .isNeutering(false)
                .microChip("4343434343434343")
                .isActive(false)
                .isRepresentative(false)
                .build();


        // 주소 목록에 주소 추가
        testMember1.addAddress(testAddress1);
        testMember2.addAddress(testAddress2);

        // CustomMemberDetails 설정
        customMemberDetails1 = new CustomMemberDetails(testMember1);
        customMemberDetails2 = new CustomMemberDetails(testMember2);

        // Mock 객체 설정
        when(memberRepository.findByUserId(new UserId("testId1234"))).thenReturn(Optional.of(testMember1));
        when(memberRepository.findByUserId(new UserId("asaaas11"))).thenReturn(Optional.of(testMember2));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember1));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(testMember2));
        when(memberRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long memberId = invocation.getArgument(0);
            if (memberId.equals(testMember1.getMemberId())) {
                return Optional.of(testMember1);
            } else if (memberId.equals(testMember2.getMemberId())) {
                return Optional.of(testMember2);
            }
            return Optional.empty();
        });

        // passwordEncoder Mock 설정
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
    }



    @Test
    @DisplayName("내 반려동물 목록 조회")
    void getAllMyPets() {
        // given
        PageRequest pageRequest = new PageRequest();
        List<Pet> petList = new ArrayList<>();
        petList.add(testPet1);
        petList.add(testPet2);
        Page<Pet> pets = new PageImpl<>(petList);

        // 적절한 mock 설정
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));
        when(petRepository.findAllPetsByMemberId(anyLong(), any(Pageable.class))).thenReturn(pets);

        // when
        Page<PetResponse> result = petService.getAllMyPets(pageRequest, customMemberDetails1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isGreaterThan(0); // 반환된 리스트가 비어있지 않은지 확인
        assertThat(result.getContent().get(0).getName()).isEqualTo("멍멍이0"); // 올바른 이름인지 확인
        assertThat(result.getContent().get(1).getName()).isEqualTo("야옹이1"); // 올바른 이름인지 확인
    }


    @Test
    @DisplayName("반려동물 조회 - 본인 반려동물")
    void getPet_MyPet() {
        // Given
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet1));
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));

        // When

        PetResponse result = petService.getPet(1L, customMemberDetails1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("멍멍이0");
        assertThat(result.getPetType()).isEqualTo(PetType.DOG);
    }

    @Test
    @DisplayName("반려동물 조회 - 타인 반려동물")
    void getPet_OtherPet() {
        // Given
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet3));
        when(petRepository.findById(4L)).thenReturn(Optional.of(testPet4));
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));

        // When
        PetResponse result = petService.getPet(3L, customMemberDetails1);
        // When & Then
        MallangsCustomException exception = assertThrows(MallangsCustomException.class, () -> {
            petService.getPet(4L, customMemberDetails1);
        });


        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("멍멍이2");
        assertThat(result.getPetType()).isEqualTo(PetType.DOG);

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PET_NOT_ACTIVATE);
    }

    @Test
    @DisplayName("대표 반려동물 조회")
    void getRepresentativePet() {
        // Given
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));
        when(petRepository.findRepresentativePetByMemberId(anyLong())).thenReturn(Optional.of(testPet1));
        //when
        PetResponse result = petService.getRepresentativePet(customMemberDetails1);
        //then
        assertThat(result.getName()).isEqualTo("멍멍이0");

    }

    @Test
    @DisplayName("반려동물 등록")
    void createPet() {
        // Given
        PetCreateRequest request = PetCreateRequest.builder()
                .name("새로운 야옹이")
                .petType(PetType.CAT)
                .gender(PetGender.FEMALE)
                .birthdate(LocalDate.of(2013,4,4))
                .weight(3.0)
                .description("새로운 야옹이")
                .isOpenProfile(false)
                .isNeutering(false)
                .microChip("4343434343434343")
                .build();

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet pet = invocation.getArgument(0);
            return Pet.builder() // Pet 객체를 생성하여 반환
                    .petId(6L)
                    .name(pet.getName())
                    .petType(pet.getPetType())
                    .gender(pet.getGender())
                    .birthdate(pet.getBirthdate())
                    .weight(pet.getWeight())
                    .description(pet.getDescription())
                    .isOpenProfile(pet.getIsOpenProfile())
                    .isNeutering(pet.getIsNeutering())
                    .microChip(pet.getMicroChip())
                    .build();
        });
        when(petRepository.existsByMemberId(anyLong())).thenReturn(false);
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));
        // When
        PetResponse result = petService.createPet(request, customMemberDetails1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("새로운 야옹이");
        assertThat(result.getPetType()).isEqualTo(PetType.CAT);
    }

    @Test
    @DisplayName("대표 반려동물 설정")
    void createRepresentativePet() {
        // Given
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));
        when(petRepository.findRepresentativePetByMemberId(anyLong())).thenReturn(Optional.of(testPet1));
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet2));

        // When
        petService.createRepresentativePet(customMemberDetails1, 2L);

        // Then
        verify(petRepository).findRepresentativePetByMemberId(testMember1.getMemberId());
        verify(petRepository).findById(2L);

        assertThat(testPet1.getIsRepresentative()).isFalse();
        assertThat(testPet2.getIsRepresentative()).isTrue();
    }

    @Test
    @DisplayName("반려동물 정보 수정")
    void updatePet() {
        // Given
        PetUpdateRequest petUpdateRequest = PetUpdateRequest.builder()
                .name("수정된 댕댕이")
                .petType(PetType.DOG)
                .gender(PetGender.FEMALE)
                .birthdate(LocalDate.of(2013,4,4))
                .weight(3.0)
                .description("수정된 댕댕이")
                .isOpenProfile(false)
                .isNeutering(false)
                .microChip("4343434343434343")
                .build();
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet1));
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));

        // When
        PetResponse result = petService.updatePet(petUpdateRequest, 1L, customMemberDetails1);

        // Then
        assertThat(result.getName()).isEqualTo("수정된 댕댕이");
        assertThat(result.getImage()).isEqualTo("test.png"); // 이미지 URL 확인
        assertThat(result.getBirthdate()).isEqualTo(LocalDate.of(2020, 1, 1));

    }

    @Test
    @DisplayName("반려동물 삭제 (비활성화)")
    void deletePet() {

        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet1));
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));

        PetResponse result = petService.deletePet(1L, customMemberDetails1);
        assertThat(result.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("반려동물 복원 (활성화)")
    void restorePet() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(testPet2));
        when(memberRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(testMember1));

        PetResponse result = petService.restorePet(2L, customMemberDetails1);
        assertThat(result.getIsActive()).isTrue();

    }

    @Test
    @DisplayName("반경 내 반려동물 조회")
    void getNearbyPets() {
        // Given
        PageRequest pageRequest = new PageRequest();
        PetLocationRequest petLocationRequest = new PetLocationRequest(127.0276, 37.4979, 20.0,"서울","강남구","역삼동");

        List<Pet> petList = new ArrayList<>();
        petList.add(testPet1);
        Page<Pet> pets = new PageImpl<>(petList);
        when(petRepository.findNearbyPets(anyDouble(), anyDouble(), anyDouble(), anyString(), anyString(), anyString(), any(Pageable.class))).thenReturn(pets);

        // When
        Page<PetNearbyResponse> result = petService.getNearbyPets(petLocationRequest, pageRequest);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("멍멍이0");

    }
}
