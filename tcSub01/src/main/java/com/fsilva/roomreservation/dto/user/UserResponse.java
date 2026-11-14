package com.fsilva.roomreservation.dto.user;

import com.fsilva.roomreservation.domain.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados públicos do usuário")
public record UserResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Maria Silva") String name,
        @Schema(example = "maria.silva@example.com") String email,
        @Schema(example = "USUARIO") UserRole role,
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime createdAt) {
}
