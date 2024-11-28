package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipatedRoomRepository extends JpaRepository<ParticipatedRoom, Long> {

    @Query("SELECT DISTINCT p FROM ParticipatedRoom p join fetch p.participant m join fetch p.chatRoom c WHERE p.participant.memberId = :memberId")
    List<ParticipatedRoom> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT DISTINCT p FROM ParticipatedRoom p join fetch p.participant m join fetch p.chatRoom c WHERE p.participatedRoomId = :participatedRoomId")
    Optional<ParticipatedRoom> findByParticipatedRoomId(@Param("participatedRoomId") Long participatedRoomId);
}
