package com.fsilva.roomreservation.dto.notification;

import com.fsilva.roomreservation.domain.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Notificação interna do usuário")
public record NotificationResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long userId,
        @Schema(example = "1") Long reservationId,
        @Schema(example = "RESERVA_CONFIRMADA") NotificationType type,
        @Schema(example = "Reserva confirmada para a sala Sala Azul em 2026-10-26T10:00:00") String message,
        @Schema(example = "false") boolean read,
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime createdAt) {
}
