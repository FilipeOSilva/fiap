package com.fsilva.roomreservation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Dados para alteração de senha")
public class ChangePasswordRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(example = "senha123")
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(example = "novaSenha123")
    private String newPassword;
}
