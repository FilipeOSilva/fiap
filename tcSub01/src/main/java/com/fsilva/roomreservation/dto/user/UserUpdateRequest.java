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
@Schema(description = "Dados para atualização do usuário")
public class UserUpdateRequest {

    @NotBlank
    @Size(max = 120)
    @Schema(example = "Maria Souza")
    private String name;

    @NotBlank
    @Email
    @Size(max = 180)
    @Schema(example = "maria.souza@example.com")
    private String email;
}
