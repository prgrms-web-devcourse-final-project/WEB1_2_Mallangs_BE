package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT r FROM ChatRoom r " +
            "LEFT JOIN  r.occupiedRooms p " +
            "LEFT JOIN  p.participant m " +
            "WHERE r.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findChatRoomsByChatRoomId(@Param("chatRoomId") Long chatRoomId);


}
