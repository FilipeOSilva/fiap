package com.fsilva.tc02.controller;

import com.fsilva.tc02.dto.RequestUserDTO;
import com.fsilva.tc02.dto.ChangePasswordDTO;
import com.fsilva.tc02.dto.UserResponseDTO;
import com.fsilva.tc02.infra.security.SecurityConfigurations;
import com.fsilva.tc02.repository.UserRepository;
import com.fsilva.tc02.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fsilva.tc02.mapper.UserMapper;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/usuarios")
@Tag(
        name = "Usuários",
        description = "Controle sobre os usuários do sistema"
)
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class UserController {
    @Autowired
    private UserRepository repository;
    private UserService service;

    @GetMapping
    @Operation(
            summary = "Lista todos os usuários",
            description = "Retorna a lista de todos os usuários cadastrados no sistema"
    )
    public ResponseEntity getAllUsers() {
        var allUsers = repository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    @Operation(
            summary = "Cria usuário",
            description = "Realiza a criação de um usuário do tipo dono do restaurante"
    )
    public ResponseEntity registerUser(@RequestBody @Valid RequestUserDTO data) {
        System.out.println(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Edita um usuário",
            description = "Realiza a edição de um usuário cadastrado"
    )
    public ResponseEntity editUser(@RequestBody @Valid RequestUserDTO data) {
        System.out.println(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    @Operation(
            summary = "Altera a senha de um usuário",
            description = "Realiza a alteração da senha de um usuário cadastrado"
    )
    public ResponseEntity<Void> changePassword(
            @PathVariable String id,
            @RequestBody @Valid ChangePasswordDTO dto) {
        service.changePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    @Operation(
            summary = "Busca usuário pelo nome",
            description = "Realiza a busca de um usuário pelo nome"
    )
    public List<UserResponseDTO> search(@RequestParam String name) {
        return service.searchByName(name)
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @DeleteMapping
    @Operation(
            summary = "Deleta um usuário",
            description = "Realiza a exclusão de um usuário"
    )
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
