package com.fsilva.roomreservation.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padrão de erro")
public record ErrorResponse(
        @Schema(example = "2026-10-25T10:00:00") LocalDateTime timestamp,
        @Schema(example = "400") int status,
        @Schema(example = "Bad Request") String error,
        @Schema(example = "Dados inválidos na requisição") String message,
        @Schema(example = "/api/users") String path,
        @Schema(example = "{\"email\":\"deve ser um endereço de e-mail válido\"}") Map<String, String> fieldErrors) {
}
