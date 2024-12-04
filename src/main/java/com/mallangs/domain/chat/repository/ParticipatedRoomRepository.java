package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ParticipatedRoomRepository extends JpaRepository<ParticipatedRoom, Long> {

    //회원 Id로 참여 채팅방 찾기
    @Query("SELECT p FROM ParticipatedRoom p" +
            " JOIN FETCH p.participant m" +
            " JOIN FETCH p.chatRoom c " +
            "WHERE p.participant.memberId = :memberId")
    List<ParticipatedRoom> findByMemberId(@Param("memberId") Long memberId);

    // 참여채팅방 Id로 참여채팅방 불러오기- 회원, 채팅방 조인
    @Query("SELECT DISTINCT p FROM ParticipatedRoom p" +
            " LEFT JOIN FETCH p.participant m" +
            " LEFT JOIN FETCH p.chatRoom c" +
            " WHERE p.participatedRoomId = :participatedRoomId")
    Optional<ParticipatedRoom> findByParticipatedRoomId(@Param("participatedRoomId") Long participatedRoomId);

    //참여채팅방 ID로 참여채팅방 불러오기 - 회원 join
    @Query("SELECT DISTINCT p FROM ParticipatedRoom p" +
            " LEFT JOIN FETCH p.participant m" +
            "  WHERE p.participatedRoomId = :participatedRoomId")
    Optional<ParticipatedRoom> findByPRoomId(@Param("participatedRoomId") Long participatedRoomId);

}
