package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT r FROM ChatRoom r " +
            "LEFT JOIN  r.occupiedRooms p " +
            "LEFT JOIN  p.participant m " +
            "LEFT JOIN  p.messages cm " +
            "LEFT JOIN  cm.isRead ir " +
            "LEFT JOIN  cm.messageImage img " +
            "WHERE m.memberId = :memberId")
    List<ChatRoom> findChatRoomsByMemberId(@Param("memberId") Long memberId);


}
