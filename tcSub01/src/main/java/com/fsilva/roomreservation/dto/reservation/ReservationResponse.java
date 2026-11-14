package com.fsilva.roomreservation.dto.reservation;

import com.fsilva.roomreservation.domain.reservation.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados da reserva")
public record ReservationResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long userId,
        @Schema(example = "Maria Silva") String userName,
        @Schema(example = "1") Long roomId,
        @Schema(example = "Sala Azul") String roomName,
        @Schema(example = "2026-10-26T10:00:00") LocalDateTime startTime,
        @Schema(example = "2026-10-26T11:00:00") LocalDateTime endTime,
        @Schema(example = "CONFIRMADA") ReservationStatus status,
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime createdAt,
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime updatedAt) {
}
