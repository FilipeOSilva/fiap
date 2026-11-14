package com.fsilva.roomreservation.mapper;

import com.fsilva.roomreservation.domain.reservation.Reservation;
import com.fsilva.roomreservation.dto.reservation.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getName(),
                reservation.getRoom().getId(),
                reservation.getRoom().getName(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt());
    }
}
