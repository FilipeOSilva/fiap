package com.fsilva.roomreservation.controller;

import com.fsilva.roomreservation.dto.error.ErrorResponse;
import com.fsilva.roomreservation.dto.user.ChangePasswordRequest;
import com.fsilva.roomreservation.dto.user.LoginRequest;
import com.fsilva.roomreservation.dto.user.LoginResponse;
import com.fsilva.roomreservation.dto.user.UserCreateRequest;
import com.fsilva.roomreservation.dto.user.UserResponse;
import com.fsilva.roomreservation.dto.user.UserUpdateRequest;
import com.fsilva.roomreservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Cadastro, atualização, busca, senha e login")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um usuário",
            description = "Valida os campos, impede e-mail duplicado e armazena a senha com BCrypt.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(name = "Sucesso", value = """
                                    {
                                      "id": 1,
                                      "name": "Maria Silva",
                                      "email": "maria.silva@example.com",
                                      "role": "USUARIO",
                                      "createdAt": "2026-10-25T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Validação", value = """
                                    {
                                      "timestamp": "2026-10-25T10:00:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Dados inválidos na requisição",
                                      "path": "/api/users",
                                      "fieldErrors": {
                                        "email": "deve ser um endereço de e-mail válido",
                                        "password": "o tamanho deve ser entre 6 e 100"
                                      }
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "E-mail duplicado", value = """
                                    {
                                      "timestamp": "2026-10-25T10:00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "message": "E-mail já cadastrado",
                                      "path": "/api/users"
                                    }
                                    """)))
    })
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Busca usuários pelo nome",
            description = "Busca case-insensitive por parte do nome; termo vazio retorna todos.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    public List<UserResponse> search(
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        return userService.searchByName(name);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza dados do usuário",
            description = "Endpoint distinto da alteração de senha; valida e-mail duplicado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @PatchMapping(value = "/{id}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Altera a senha do usuário",
            description = "Exige a senha atual e rejeita nova senha igual à atual.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha alterada"),
            @ApiResponse(responseCode = "400", description = "Senha atual inválida ou nova senha inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void changePassword(
            @PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Valida credenciais de login",
            description = "Retorna os dados do usuário; nesta fase não emite token JWT (desafio extra).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login validado",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(name = "Sucesso", value = """
                                    {
                                      "id": 1,
                                      "name": "Maria Silva",
                                      "email": "maria.silva@example.com",
                                      "role": "USUARIO",
                                      "message": "Login validado com sucesso"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Falha", value = """
                                    {
                                      "timestamp": "2026-10-25T10:00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Credenciais inválidas",
                                      "path": "/api/users/login"
                                    }
                                    """)))
    })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
