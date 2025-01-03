package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    //채팅 수정
    @Query("SELECT DISTINCT c FROM ChatMessage c " +
            "LEFT JOIN FETCH  c.chatRoom r " +
            "LEFT JOIN FETCH c.sender " +
            "WHERE c.chatMessageId =:chatMessageId")
    Optional<ChatMessage> findByChatMessageId(@Param("chatMessageId") Long chatMessageId);

    //채팅 조회
    @Query("SELECT DISTINCT c FROM ChatMessage c " +
            "JOIN FETCH c.chatRoom r " +
            "JOIN FETCH c.sender " +
            "WHERE r.chatRoomId = :chatRoomId")
    Page<ChatMessage> findMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    //채팅 조회 시간 순
    @Query("SELECT c FROM ChatMessage c " +
            "JOIN FETCH c.chatRoom r " +
            "WHERE r.chatRoomId = :chatRoomId " +
            "ORDER BY c.createdAt DESC")
    List<ChatMessage> findMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Modifying
    @Transactional
    @Query(" UPDATE ChatMessage c SET c.receiverRead = true " +
            " WHERE c.chatRoom.chatRoomId = :chatRoomId " +
            " AND c.receiverRead = false " +
            " AND c.sender.nickname.value != :nickname")
    int updateRead(@Param("chatRoomId") Long chatRoomId,
                   @Param("nickname") String nickname);
}
