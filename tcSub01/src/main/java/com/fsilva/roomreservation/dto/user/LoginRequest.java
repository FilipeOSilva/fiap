package com.fsilva.roomreservation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Credenciais de login")
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(example = "maria.silva@example.com")
    private String email;

    @NotBlank
    @Size(max = 100)
    @Schema(example = "senha123")
    private String password;
}
