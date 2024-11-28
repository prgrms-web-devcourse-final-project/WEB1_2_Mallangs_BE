package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ChatRoom;
import com.mallangs.domain.chat.entity.ParticipatedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
