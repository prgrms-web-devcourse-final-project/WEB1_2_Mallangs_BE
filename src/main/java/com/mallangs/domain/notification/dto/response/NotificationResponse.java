package com.mallangs.domain.notification.dto.response;

import com.mallangs.domain.notification.entity.Notification;
import com.mallangs.domain.notification.entity.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse {
    private Long notificationId;
    private String message;
    private NotificationType notificationType;
    private String url;
    private Boolean isRead;

    public NotificationResponse(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.message = notification.getMessage();
        this.notificationType = notification.getNotificationType();
        this.url = notification.getUrl();
        this.isRead = notification.getIsRead();
    }
}