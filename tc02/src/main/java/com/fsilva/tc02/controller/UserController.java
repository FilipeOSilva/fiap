package com.fsilva.tc02.controller;

import com.fsilva.tc02.dto.RequestUserDTO;
import com.fsilva.tc02.infra.security.SecurityConfigurations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/usuarios")
@Tag(
        name = "Usuários",
        description = "Controle sobre os usuários do sistema"
)
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class UserController {

    @GetMapping
    @Operation(
            summary = "Lista todos os usuários",
            description = "Retorna a lista de todos os usuários cadastrados no sistema"
    )
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping
    @Operation(
            summary = "Cria usuário",
            description = "Realiza a criação de um usuário do tipo dono do restaurante"
    )
    public ResponseEntity registerUser() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Edita um usuário",
            description = "Realiza a edição de um usuário cadastrado"
    )
    public ResponseEntity editUser(@RequestBody @Valid RequestUserDTO data) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    @Operation(
            summary = "Altera a senha de um usuário",
            description = "Realiza a alteração da senha de um usuário cadastrado"
    )
    public ResponseEntity<Void> changePassword() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nome")
    @Operation(
            summary = "Busca usuário pelo nome",
            description = "Realiza a busca de um usuário pelo nome"
    )
    public ResponseEntity search(@RequestParam String name) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Deleta um usuário",
            description = "Realiza a exclusão de um usuário"
    )
    public ResponseEntity delete(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }
}
