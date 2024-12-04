package com.mallangs.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    @NotBlank
    private Long notificationId;

    @NotBlank
    private Long MemberId;
}