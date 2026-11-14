package com.fsilva.roomreservation.infra.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI roomReservationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Room Reservation API")
                        .description("""
                                Sistema de reserva de salas de reuniões (Tech Challenge - Fase 1).
                                Gerencia usuários, salas, disponibilidade, reservas e notificações internas.
                                A identidade do ator em alterações/cancelamentos é informada pelo header X-User-Id.
                                """)
                        .version("0.0.1-SNAPSHOT"))
                .addServersItem(new Server().url("http://localhost:8080").description("Ambiente local"));
    }
}
