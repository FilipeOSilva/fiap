package com.fsilva.roomreservation.service;

import com.fsilva.roomreservation.domain.room.RoomResource;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import com.fsilva.roomreservation.dto.room.RoomRequest;
import com.fsilva.roomreservation.dto.room.RoomResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    RoomResponse create(RoomRequest request);

    RoomResponse update(Long id, RoomRequest request);

    RoomResponse findById(Long id);

    List<RoomResponse> findAll(RoomStatus status, RoomResource resource, Integer minCapacity);

    List<RoomResponse> findAvailable(
            LocalDateTime start, LocalDateTime end, RoomResource resource, Integer minCapacity);
}
