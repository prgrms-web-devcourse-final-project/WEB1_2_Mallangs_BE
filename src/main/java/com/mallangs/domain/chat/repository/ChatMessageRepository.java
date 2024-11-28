package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
