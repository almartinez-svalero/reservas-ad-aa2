# Checklist de entrega - AA1 Acceso a Datos

## Requisitos obligatorios

| Requisito | Estado | Dónde está |
|---|---:|---|
| API Spring Boot conectada a BD relacional | ✅ | `pom.xml`, `application.properties`, `docker-compose.yml` con PostgreSQL |
| No usa SQLite/H2 | ✅ | PostgreSQL en `docker-compose.yml` |
| Mínimo 5 clases | ✅ | `Restaurante`, `Mesa`, `Cliente`, `Empleado`, `Reserva` en `src/main/java/com/aa/reservas/model` |
| Relaciones entre clases | ✅ | `Mesa -> Restaurante`, `Empleado -> Restaurante`, `Reserva -> Cliente/Mesa/Empleado` |
| 6 atributos por clase | ✅ | Todas las entidades tienen 8 o más campos |
| Atributos obligatorios y validaciones | ✅ | Anotaciones `@NotBlank`, `@NotNull`, `@Email`, `@Pattern`, `@Min`, etc. |
| CRUD completo de cada clase | ✅ | Controllers en `src/main/java/com/aa/reservas/controller` |
| Errores 400, 404, 500 | ✅ | `GlobalExceptionHandler.java` |
| OpenAPI 3.0 | ✅ | `docs/openapi.yaml` |
| API Mock con WireMock | ✅ | `wiremock/mappings` |
| Tests Service de las 5 clases | ✅ | `src/test/java/com/aa/reservas/service` |
| Tests Controller 20X, 400 y 404 | ✅ | `src/test/java/com/aa/reservas/controller` |
| Filtros GET con hasta 3 campos por clase | ✅ | Métodos `findAll` en controllers y services |
| Colección Postman | ✅ | `postman/reservas-api.postman_collection.json` |

## Funcionalidades extra incluidas

| Extra | Estado | Dónde está |
|---|---:|---|
| PATCH para cada clase | ✅ | `@PatchMapping` en los 5 controllers |
| Git/GitHub/README/Git Flow | ✅ parcial | `README.md` explica flujo Git Flow. El repositorio remoto hay que crearlo manualmente en GitHub. |
| 3 endpoints nuevos usando DTOs/relaciones | ✅ | `GET /restaurantes/{id}/resumen`, `GET /clientes/{id}/historial`, `POST /reservas/asignar` |
| 3 endpoints con JPQL | ✅ | Repositories: `ClienteRepository`, `MesaRepository`, `ReservaRepository` |
| 3 endpoints con SQL nativo | ✅ | Repositories: `RestauranteRepository`, `EmpleadoRepository`, `ReservaRepository` |
| Logs de operaciones y errores | ✅ | `LoggingAspect.java` y `GlobalExceptionHandler.java` |
| Postman parametrizado | ✅ | Variables en colección y entorno Postman |
| Ejemplos en OpenAPI | ✅ | `docs/openapi.yaml` incluye ejemplos en requestBodies |

## Funcionalidades no incluidas

No se ha incluido JWT, subida/descarga de ficheros ni 2 clases extra porque el objetivo era un proyecto sólido y defendible para una nota aproximada de 8, sin hacerlo innecesariamente más complejo.
