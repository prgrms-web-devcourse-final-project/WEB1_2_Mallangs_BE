package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.ParticipatedRoom;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipatedRoomRepository extends JpaRepository<ParticipatedRoom, Long> {
}
