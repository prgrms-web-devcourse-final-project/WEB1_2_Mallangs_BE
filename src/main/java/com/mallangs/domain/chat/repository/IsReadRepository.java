package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.IsRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IsReadRepository extends JpaRepository<IsRead, Long> {

    //기준 아이디 이후의 아이디만 읽기로 바꾸기
    @Modifying
    @Transactional
    @Query("UPDATE IsRead i SET i.readCheck = true " +
            "WHERE i.chatMessage.chatMessageId > :chatMessageId" +
            " AND i.chatMessage.participatedRoom.participatedRoomId = :participatedRoomId" +
            " AND i.reader = :reader" +
            " AND i.readCheck = false ")
    int turnUnReadToRead(@Param("chatMessageId") Long chatMessageId,
                         @Param("participatedRoomId") Long participatedRoomId,
                         @Param("reader") String reader);

}
