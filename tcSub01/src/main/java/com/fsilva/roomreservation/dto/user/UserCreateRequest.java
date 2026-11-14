package com.fsilva.roomreservation.dto.user;

import com.fsilva.roomreservation.domain.user.UserRole;
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
@Schema(description = "Dados para cadastro de usuário")
public class UserCreateRequest {

    @NotBlank
    @Size(max = 120)
    @Schema(example = "Maria Silva")
    private String name;

    @NotBlank
    @Email
    @Size(max = 180)
    @Schema(example = "maria.silva@example.com")
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(example = "senha123")
    private String password;

    @Schema(description = "Papel do usuário; se omitido, assume USUARIO", example = "USUARIO")
    private UserRole role;
}
