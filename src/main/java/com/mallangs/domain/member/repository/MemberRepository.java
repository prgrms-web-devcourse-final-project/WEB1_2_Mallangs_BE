package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.repository.search.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberSearch {
    Member findByUserId(String userId);
    Boolean existsByUserId(String userId);
    Boolean existsByEmail(Email email);
}
