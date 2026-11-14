package com.fsilva.roomreservation.dto.room;

import com.fsilva.roomreservation.domain.room.RoomResource;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Dados da sala")
public record RoomResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Sala Azul") String name,
        @Schema(example = "12") Integer capacity,
        @Schema(example = "3º andar - Ala Norte") String location,
        @Schema(example = "[\"PROJETOR\", \"VIDEOCONFERENCIA\"]") Set<RoomResource> resources,
        @Schema(example = "ATIVA") RoomStatus status,
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime createdAt) {
}
