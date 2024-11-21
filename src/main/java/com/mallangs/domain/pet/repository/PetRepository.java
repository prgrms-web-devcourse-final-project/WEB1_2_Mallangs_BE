package com.mallangs.domain.pet.repository;

import com.mallangs.domain.pet.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.member m WHERE m.memberId = :memberId AND p.isActive = true")
    Page<Pet> findAllPetsByMemberId(@Param("memberId") Long memberId, Pageable pageable); //본인 반려동물 조회

    @Query("SELECT p FROM Pet p WHERE p.isOpenProfile = TRUE AND p.isActive = true")
    Page<Pet> findAllOpenProfilePets(Pageable pageable);

//    @Query("SELECT p FROM Pet p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
//    Page<Pet> findByPetName(@Param("name") String name, Pageable pageable); //반려동물 검색 (보류)



}
