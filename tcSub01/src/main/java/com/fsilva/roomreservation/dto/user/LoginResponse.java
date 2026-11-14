package com.fsilva.roomreservation.dto.user;

import com.fsilva.roomreservation.domain.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado da validação de login (sem token nesta fase)")
public record LoginResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Maria Silva") String name,
        @Schema(example = "maria.silva@example.com") String email,
        @Schema(example = "USUARIO") UserRole role,
        @Schema(example = "Login validado com sucesso") String message) {
}
