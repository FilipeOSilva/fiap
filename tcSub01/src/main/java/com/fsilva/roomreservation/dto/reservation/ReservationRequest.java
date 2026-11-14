package com.fsilva.roomreservation.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Dados para solicitação de reserva")
public class ReservationRequest {

    @NotNull
    @Schema(example = "1")
    private Long userId;

    @NotNull
    @Schema(example = "1")
    private Long roomId;

    @NotNull
    @Schema(example = "2026-10-26T10:00:00")
    private LocalDateTime startTime;

    @NotNull
    @Schema(example = "2026-10-26T11:00:00")
    private LocalDateTime endTime;
}
