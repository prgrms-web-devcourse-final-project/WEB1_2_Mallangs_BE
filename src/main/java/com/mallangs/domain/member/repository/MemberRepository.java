package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUserId(UserId userId);
    Boolean existsByEmail(Email email);

    //단일 회원조회 - 아이디
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.memberId =:memberId")
    Optional<Member> findByMemberId(@Param("memberId") Long memberId);

    //단일 회원조회 - 이메일
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.email =:email")
    Optional<Member> findByEmail(@Param("email") Email email);

    //단일 회원조회 - 유저아이디
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.userId =:userId")
    Optional<Member> findByUserId(@Param("userId") UserId userId);

    //단일 회원조회 - 아이디 찾기
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.email =:email AND m.nickname =:nickname")
    Optional<Member> findByEmailAndNickname(@Param("email") Email email, @Param("nickname") Nickname nickname);

    //단일 회원조회 - 비밀번호 찾기
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.email =:email AND m.userId =:userId")
    Optional<Member> findByEmailAndUserId(@Param("email") Email email, @Param("userId") UserId userId);

    //모든 회원조회+주소
    @Query("SELECT m FROM Member m join fetch m.addresses")
    List<Member> memberList();

    //회원 userId로 검색
    @Query("SELECT m FROM Member m join fetch m.addresses where m.userId.value LIKE CONCAT('%',:userIdValue,'%') ")
    List<Member> searchMemberByUserId(@Param("userIdValue") String userIdValue);

    //회원 email로 검색
    @Query("SELECT m FROM Member m join fetch m.addresses where m.email.value LIKE CONCAT('%',:emailValue,'%') ")
    List<Member> searchMemberByEmail(@Param("emailValue") String emailValue);

    //회원 nickname으로 검색
    @Query("SELECT m FROM Member m join fetch m.addresses where m.nickname.value LIKE CONCAT('%',:nicknameValue,'%') ")
    List<Member> searchMemberByNickname(@Param("nicknameValue") String nicknameValue);


}
