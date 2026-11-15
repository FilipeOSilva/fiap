# Room Reservation

Sistema de reserva de salas de reuniões desenvolvido para o Tech Challenge (Reposição da Fase 1).

Permite cadastrar salas, consultar disponibilidade, criar, alterar e cancelar reservas e registrar notificações internas. A aplicação impede reservas sobrepostas para a mesma sala e retorna HTTP 409 em caso de conflito.

**Stack:** Java 21, Spring Boot 4.1.1, PostgreSQL 17, Flyway, Maven e Docker Compose. A API é documentada com Swagger/OpenAPI.

## Como rodar

### Com Docker (aplicação + banco)

Pré-requisitos: Docker e Docker Compose.

```bash
docker compose up -d --build
```

Após subir, acesse:

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Para parar:

```bash
docker compose down
```

Para parar e remover também os dados do banco:

```bash
docker compose down -v
```