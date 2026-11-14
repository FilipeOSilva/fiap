package com.fsilva.roomreservation.mapper;

import com.fsilva.roomreservation.domain.notification.Notification;
import com.fsilva.roomreservation.dto.notification.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUser().getId(),
                notification.getReservation() == null ? null : notification.getReservation().getId(),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt());
    }
}
