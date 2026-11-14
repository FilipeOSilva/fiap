package com.fsilva.roomreservation.repository;

import com.fsilva.roomreservation.domain.reservation.Reservation;
import com.fsilva.roomreservation.domain.reservation.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByRoomIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId,
            ReservationStatus status,
            LocalDateTime end,
            LocalDateTime start);

    boolean existsByRoomIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
            Long roomId,
            ReservationStatus status,
            LocalDateTime end,
            LocalDateTime start,
            Long id);

    List<Reservation> findByUserIdOrderByStartTimeDesc(Long userId);

    List<Reservation> findByRoomIdOrderByStartTimeAsc(Long roomId);

    List<Reservation> findByStatusAndReminderSentFalseAndStartTimeBetween(
            ReservationStatus status,
            LocalDateTime from,
            LocalDateTime to);
}
