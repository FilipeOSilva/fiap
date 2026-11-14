package com.fsilva.roomreservation.controller;

import com.fsilva.roomreservation.domain.room.RoomResource;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import com.fsilva.roomreservation.dto.error.ErrorResponse;
import com.fsilva.roomreservation.dto.room.RoomRequest;
import com.fsilva.roomreservation.dto.room.RoomResponse;
import com.fsilva.roomreservation.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Cadastro, consulta e disponibilidade de salas")
public class RoomController {

    private final RoomService roomService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma sala",
            description = "Nome único, capacidade maior que zero, localização e recursos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sala cadastrada",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class),
                            examples = @ExampleObject(name = "Sucesso", value = """
                                    {
                                      "id": 1,
                                      "name": "Sala Azul",
                                      "capacity": 12,
                                      "location": "3º andar - Ala Norte",
                                      "resources": ["PROJETOR", "VIDEOCONFERENCIA"],
                                      "status": "ATIVA",
                                      "createdAt": "2026-10-25T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Nome de sala já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Nome duplicado", value = """
                                    {
                                      "timestamp": "2026-10-25T10:00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "message": "Já existe uma sala com esse nome",
                                      "path": "/api/rooms"
                                    }
                                    """)))
    })
    public RoomResponse create(@Valid @RequestBody RoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca sala por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala encontrada",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public RoomResponse findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Lista salas com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de salas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class))))
    public List<RoomResponse> findAll(
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(required = false) RoomResource resource,
            @RequestParam(required = false) Integer minCapacity) {
        return roomService.findAll(status, resource, minCapacity);
    }

    @GetMapping("/available")
    @Operation(summary = "Lista salas disponíveis em uma faixa de horário",
            description = "Retorna salas ativas sem reserva confirmada sobreposta à faixa informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salas disponíveis",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class)),
                            examples = @ExampleObject(name = "Sucesso", value = """
                                    [
                                      {
                                        "id": 1,
                                        "name": "Sala Azul",
                                        "capacity": 12,
                                        "location": "3º andar - Ala Norte",
                                        "resources": ["PROJETOR", "VIDEOCONFERENCIA"],
                                        "status": "ATIVA",
                                        "createdAt": "2026-10-25T10:00:00"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Faixa de horário inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<RoomResponse> findAvailable(
            @RequestParam
            @DateTimeFormat(iso = ISO.DATE_TIME)
            @Parameter(description = "Início da faixa", example = "2026-10-26T10:00:00")
            LocalDateTime start,
            @RequestParam
            @DateTimeFormat(iso = ISO.DATE_TIME)
            @Parameter(description = "Fim da faixa", example = "2026-10-26T11:00:00")
            LocalDateTime end,
            @RequestParam(required = false) RoomResource resource,
            @RequestParam(required = false) Integer minCapacity) {
        return roomService.findAvailable(start, end, resource, minCapacity);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza uma sala", description = "Também permite ativar/inativar via status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala atualizada",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Nome de sala já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public RoomResponse update(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return roomService.update(id, request);
    }
}
