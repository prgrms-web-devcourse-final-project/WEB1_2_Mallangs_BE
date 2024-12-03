package com.mallangs.domain.member.repository;

import com.mallangs.domain.member.dto.MemberGetResponseOnlyMember;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.embadded.Email;
import com.mallangs.domain.member.entity.embadded.Nickname;
import com.mallangs.domain.member.entity.embadded.Password;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUserId(UserId userId);
    Boolean existsByEmail(Email email);

    //오직 회원만 조회
    @Query("SELECT m FROM Member m WHERE m.userId =:userId")
    Optional<Member> findMemberOnlyByUserId(@Param("userId") UserId userId);

    //비밀번호 확인
    @Query("SELECT m FROM Member m WHERE m.password.value =:password")
    Optional<Member> findByPassword(@Param("password")String password);

    //단일 회원조회 - 아이디
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.memberId =:memberId")
    Optional<Member> findByMemberId(@Param("memberId") Long memberId);

    //단일 회원조회 - 이메일
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.email =:email")
    Optional<Member> findByEmail(@Param("email") Email email);

    //단일 회원조회 - 유저아이디
    @Query(" SELECT m FROM Member m JOIN FETCH m.addresses " +
            " WHERE m.userId =:userId ")
    Optional<Member> findByUserId(@Param("userId") UserId userId);

    //단일 회원조회 - 회원프로필 조회
    @Query("SELECT DISTINCT m FROM Member m LEFT JOIN FETCH m.addresses" +
            " LEFT JOIN m.pets " +
            "WHERE m.userId =:userId")
    Optional<Member> findByUserIdForProfile(@Param("userId") UserId userId);

    //단일 회원조회 - 아이디 찾기
    @Query("SELECT m FROM Member m join fetch m.addresses " +
            "WHERE m.email =:email AND m.nickname =:nickname")
    Optional<Member> findByEmailAndNickname(@Param("email") Email email, @Param("nickname") Nickname nickname);

    //단일 회원조회 - 비밀번호 찾기
    @Query("SELECT m FROM Member m join fetch m.addresses " +
            "WHERE m.email =:email AND m.userId =:userId")
    Optional<Member> findByEmailAndUserId(@Param("email") Email email, @Param("userId") UserId userId);

    //모든 회원조회+주소
    @Query("SELECT m FROM Member m join fetch m.addresses WHERE m.userId = :userId")
    List<Member> memberList();

    //모든 회원조회 - UserId
    @Query("SELECT new com.mallangs.domain.member.dto.MemberGetResponseOnlyMember(m) " +
            "FROM Member m " +
            "WHERE (:isActive IS NULL OR m.isActive = :isActive) " +
            "AND (:userId IS NULL OR m.userId.value = :userId) " +
            "AND (:createAt IS NULL OR m.createdAt >= :createAt)")
    Page<MemberGetResponseOnlyMember> memberListByUserId(
            @Param("isActive") Boolean isActive,
            @Param("userId") UserId userId,
            @Param("createAt") LocalDateTime createAt,
            Pageable pageable);

    //모든 회원조회 - Email
    @Query(" SELECT new com.mallangs.domain.member.dto.MemberGetResponseOnlyMember(m) " +
            " FROM Member m " +
            " WHERE (:isActive IS NULL OR m.isActive = :isActive) " +
            " AND (:email IS NULL OR m.email.value = :email)" +
            " AND (:createAt IS NULL OR m.createdAt >= :createAt) ")
    Page<MemberGetResponseOnlyMember> memberListByEmail(
            @Param("isActive") Boolean isActive,
            @Param("email") Email email,
            @Param("createAt") LocalDateTime createAt,
            Pageable pageable);

    //모든 회원조회 - Nickname
    @Query("SELECT new com.mallangs.domain.member.dto.MemberGetResponseOnlyMember(m) " +
            "FROM Member m " +
            "WHERE (:isActive IS NOT NULL AND m.isActive = :isActive) " +
            "AND (:nickname IS NOT NULL AND m.nickname.value LIKE CONCAT('%', :nickname, '%')) " +
            "AND (:createAt IS NOT NULL AND m.createdAt >= :createAt)")
    Page<MemberGetResponseOnlyMember> memberListByNickname(
            @Param("isActive") Boolean isActive,
            @Param("nickname") String nickname,
            @Param("createAt") LocalDateTime createAt,
            Pageable pageable);

    //벌크 - 회원 차단 - 7,15,30일 동안
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.isActive = FALSE ,m.expiryDate =:expiryDate," +
            " m.reasonForBan =:reasonForBan WHERE m.memberId IN :memberIds")
    int blockMembersForExpiryDate(@Param("expiryDate") LocalDateTime expiryDate, @Param("memberIds") List<Long> memberIds, @Param("reasonForBan") String reasonForBan);

    //벌크 - 회원 활성화
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.isActive = true, m.reasonForBan = null" +
            " WHERE m.isActive = FALSE AND m.expiryDate < :localDateTime")
    void activeBannedMembers(@Param("localDateTime") LocalDateTime localDateTime);

}
