package com.fsilva.roomreservation.controller;

import com.fsilva.roomreservation.dto.error.ErrorResponse;
import com.fsilva.roomreservation.dto.reservation.ReservationRequest;
import com.fsilva.roomreservation.dto.reservation.ReservationResponse;
import com.fsilva.roomreservation.dto.reservation.ReservationUpdateRequest;
import com.fsilva.roomreservation.service.ReservationService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Solicitação, alteração e cancelamento de reservas")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Solicita uma reserva",
            description = "Confirma automaticamente se a sala estiver livre; retorna 409 em conflito. "
                    + "Duração máxima de 8 horas e início não pode estar no passado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva confirmada",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class),
                            examples = @ExampleObject(name = "Sucesso", value = """
                                    {
                                      "id": 1,
                                      "userId": 1,
                                      "userName": "Maria Silva",
                                      "roomId": 1,
                                      "roomName": "Sala Azul",
                                      "startTime": "2026-10-26T10:00:00",
                                      "endTime": "2026-10-26T11:00:00",
                                      "status": "CONFIRMADA",
                                      "createdAt": "2026-10-25T10:00:00",
                                      "updatedAt": "2026-10-25T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Faixa inválida, sala inativa ou dados ausentes",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou sala não encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Sala indisponível no período",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Conflito", value = """
                                    {
                                      "timestamp": "2026-10-25T10:00:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "message": "Sala indisponível no período solicitado",
                                      "path": "/api/reservations"
                                    }
                                    """)))
    })
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request) {
        return reservationService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca reserva por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Lista todas as reservas", description = "Visão de administração.")
    @ApiResponse(responseCode = "200", description = "Lista de reservas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class))))
    public List<ReservationResponse> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lista reservas de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas do usuário",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ReservationResponse> findByUser(@PathVariable Long userId) {
        return reservationService.findByUser(userId);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "Lista reservas de uma sala")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas da sala",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ReservationResponse> findByRoom(@PathVariable Long roomId) {
        return reservationService.findByRoom(roomId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Altera uma reserva",
            description = "Dono ou ADMIN; revalida conflito na nova faixa de horário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva alterada",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Faixa inválida, sala inativa ou reserva cancelada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva, usuário ou sala não encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Sala indisponível na nova faixa",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse update(
            @PathVariable Long id,
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID do usuário que executa a ação", example = "1")
            Long actingUserId,
            @Valid @RequestBody ReservationUpdateRequest request) {
        return reservationService.update(id, actingUserId, request);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancela uma reserva",
            description = "Dono ou ADMIN; a faixa volta a ficar disponível e o dono é notificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Reserva já cancelada ou em status inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva ou usuário não encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ReservationResponse cancel(
            @PathVariable Long id,
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID do usuário que executa a ação", example = "1")
            Long actingUserId) {
        return reservationService.cancel(id, actingUserId);
    }
}
