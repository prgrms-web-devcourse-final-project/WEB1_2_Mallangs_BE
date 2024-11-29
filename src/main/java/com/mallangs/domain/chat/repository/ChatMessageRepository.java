package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatMessage;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    //채팅 수정
    @Query("SELECT DISTINCT c FROM ChatMessage c JOIN FETCH c.participatedRoom p join fetch  p.chatRoom r join fetch c.sender WHERE c.chatMessageId =:chatMessageId")
    Optional<ChatMessage> findByChatMessageId(@Param("chatMessageId") Long chatMessageId);

    //채팅 조회
    @Query("SELECT DISTINCT c FROM ChatMessage c JOIN FETCH c.participatedRoom p join fetch  p.chatRoom r join fetch c.sender WHERE r.chatRoomId = :chatRoomId")
    List<ChatMessage> findMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}
