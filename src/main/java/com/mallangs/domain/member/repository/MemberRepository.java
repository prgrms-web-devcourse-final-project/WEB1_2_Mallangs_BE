package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(UserId userId);
    Boolean existsByUserId(UserId userId);
    Boolean existsByEmail(Email email);
    @Query("SELECT m FROM Member m join fetch m.addresses")
    List<Member> memberList();
}
