package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    //memberId로 Address 찾기
    @Query("SELECT a FROM Address a join a.member m WHERE m.memberId =:memberId")
    List<Address> findByMemberId(@Param("memberId") Long memberId);
}
