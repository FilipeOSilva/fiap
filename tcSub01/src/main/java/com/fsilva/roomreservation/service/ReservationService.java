package com.fsilva.roomreservation.service;

import com.fsilva.roomreservation.dto.reservation.ReservationRequest;
import com.fsilva.roomreservation.dto.reservation.ReservationResponse;
import com.fsilva.roomreservation.dto.reservation.ReservationUpdateRequest;
import java.util.List;

public interface ReservationService {

    ReservationResponse create(ReservationRequest request);

    ReservationResponse update(Long id, Long actingUserId, ReservationUpdateRequest request);

    ReservationResponse cancel(Long id, Long actingUserId);

    ReservationResponse findById(Long id);

    List<ReservationResponse> findAll();

    List<ReservationResponse> findByUser(Long userId);

    List<ReservationResponse> findByRoom(Long roomId);

    void sendDueReminders();
}
