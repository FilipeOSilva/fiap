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
@Schema(description = "Dados para alteração de reserva")
public class ReservationUpdateRequest {

    @NotNull
    @Schema(example = "1")
    private Long roomId;

    @NotNull
    @Schema(example = "2026-10-26T14:00:00")
    private LocalDateTime startTime;

    @NotNull
    @Schema(example = "2026-10-26T15:00:00")
    private LocalDateTime endTime;
}
