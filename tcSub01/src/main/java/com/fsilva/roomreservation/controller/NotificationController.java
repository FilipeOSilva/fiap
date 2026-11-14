package com.fsilva.roomreservation.controller;

import com.fsilva.roomreservation.dto.error.ErrorResponse;
import com.fsilva.roomreservation.dto.notification.NotificationResponse;
import com.fsilva.roomreservation.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Notificações internas de status e lembretes")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Lista notificações de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de notificações",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<NotificationResponse> findByUser(@RequestParam Long userId) {
        return notificationService.findByUser(userId);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Marca notificação como lida",
            description = "Somente o destinatário ou um ADMIN pode marcar a notificação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificação marcada como lida",
                    content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Notificação ou usuário não encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public NotificationResponse markAsRead(
            @PathVariable Long id,
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID do usuário que executa a ação", example = "1")
            Long actingUserId) {
        return notificationService.markAsRead(id, actingUserId);
    }
}
