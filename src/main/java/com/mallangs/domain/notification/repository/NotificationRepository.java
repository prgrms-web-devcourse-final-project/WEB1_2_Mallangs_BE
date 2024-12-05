package com.mallangs.domain.notification.repository;

import com.mallangs.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 회원 별 알림 조회
    @Query("SELECT n FROM Notification n WHERE n.member.memberId = :memberId ORDER BY n.notificationId DESC")
    List<Notification> findByMemberId(@Param("memberId") Long memberId);
}