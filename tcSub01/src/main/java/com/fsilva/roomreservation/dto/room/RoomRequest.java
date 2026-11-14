package com.fsilva.roomreservation.dto.room;

import com.fsilva.roomreservation.domain.room.RoomResource;
import com.fsilva.roomreservation.domain.room.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Dados de cadastro/atualização de sala")
public class RoomRequest {

    @NotBlank
    @Size(max = 120)
    @Schema(example = "Sala Azul")
    private String name;

    @NotNull
    @Min(1)
    @Schema(example = "12")
    private Integer capacity;

    @NotBlank
    @Size(max = 180)
    @Schema(example = "3º andar - Ala Norte")
    private String location;

    @Schema(example = "[\"PROJETOR\", \"VIDEOCONFERENCIA\"]")
    private Set<RoomResource> resources;

    @Schema(description = "Status da sala; se omitido no cadastro, assume ATIVA", example = "ATIVA")
    private RoomStatus status;
}
