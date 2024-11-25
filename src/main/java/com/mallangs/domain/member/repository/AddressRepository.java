package com.mallangs.domain.member.repository;


import com.mallangs.domain.member.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
