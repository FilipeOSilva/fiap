package com.fsilva.roomreservation.mapper;

import com.fsilva.roomreservation.domain.room.Room;
import com.fsilva.roomreservation.dto.room.RoomResponse;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getCapacity(),
                room.getLocation(),
                Set.copyOf(room.getResources()),
                room.getStatus(),
                room.getCreatedAt());
    }
}
