package com.mallangs.domain.pet.repository;

import com.mallangs.domain.pet.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.member m WHERE m.memberId = :memberId ")
    Page<Pet> findAllPetsByMemberId(@Param("memberId") Long memberId, Pageable pageable); //본인 반려동물 조회

    //모든 반려동물 조회, 공개프로필과 활성상태
    @Query("SELECT p FROM Pet p WHERE p.isOpenProfile = TRUE AND p.isActive = true")
    Page<Pet> findAllOpenProfilePets(Pageable pageable);

    @Query( "SELECT COUNT(p) > 0 FROM Pet p WHERE p.member.memberId = :memberId")
    boolean existsByMemberId(Long memberId);

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.member m WHERE m.memberId = :memberId AND p.isRepresentative = true")
    Optional<Pet> findRepresentativePetByMemberId(Long memberId);

    //반경 내 반려동물 조회
    @Query(value = """
        SELECT p.* FROM Pet p \
        JOIN Member m ON p.member_id = m.member_id \
        JOIN address a ON a.member_id = m.member_id \
        WHERE p.is_open_profile = true \
        AND p.is_active = true \
        AND ST_Distance_Sphere(\
            point(?2, ?1), \
            point(ST_X(a.point), ST_Y(a.point))\
        ) <= ?3 * 1000 \
        AND (?4 IS NULL OR a.region_1depth_name = ?4) \
        AND (?5 IS NULL OR a.region_2depth_name = ?5) \
        AND (?6 IS NULL OR a.region_3depth_name = ?6)
        """,
            nativeQuery = true)
    Page<Pet> findNearbyPets(
            Double y,                  // 검색 기준 y좌표 (위도)
            Double x,                  // 검색 기준 x좌표 (경도)
            Double radiusInKm,         // 검색 반경 (km)
            String region1depthName,   // 시/도 필터
            String region2depthName,   // 구/군 필터
            String region3depthName,   // 동/읍/면 필터
            Pageable pageable         // 페이징 정보
    );


}



