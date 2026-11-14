package com.fsilva.roomreservation.service;

import com.fsilva.roomreservation.domain.reservation.Reservation;
import com.fsilva.roomreservation.dto.notification.NotificationResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    List<NotificationResponse> findByUser(Long userId);

    NotificationResponse markAsRead(Long id, Long actingUserId);

    void notifyConfirmed(Reservation reservation);

    void notifyAltered(Reservation reservation);

    void notifyCancelled(Reservation reservation);

    void notifyRejected(
            Long userId,
            String roomName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String reason);

    void notifyReminder(Reservation reservation);
}
