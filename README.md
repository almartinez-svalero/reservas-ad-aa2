# Reservas API - AA2 Acceso a Datos

Proyecto de la Actividad de Aprendizaje de 2a Evaluacion: Testing y Gestion de APIs.

La entrega parte de la API REST de reservas desarrollada en la 1a evaluacion y la amplia con versionado de endpoints, configuracion por entornos, MariaDB en produccion, Docker Compose, GitHub Actions, Postman/Newman, JWT, APIMan y preparacion de despliegue en AWS.

Repositorio:

```text
https://github.com/almartinez-svalero/reservas-ad-aa2
```

## Evidencias principales

- Informe explicativo: `docs/Informe-AA2-Requisitos.pdf`.
- Checklist AA2: `docs/checklist-aa2.md`.
- Versionado de endpoints: `src/main/java/com/aa/reservas/controller/ClienteV2Controller.java`.
- Configuracion por entornos: `src/main/resources/application-dev.properties` y `src/main/resources/application-prod.properties`.
- Produccion con MariaDB: `docker-compose.yml`, `docker-compose.aws.yml` y `application-prod.properties`.
- GitHub Actions: `.github/workflows/api-tests.yml`.
- Tests Postman de Cliente: `postman/clientes-integration.postman_collection.json`.
- Tests Postman completos: `postman/all-entities-integration.postman_collection.json`.
- APIMan: `docker-compose.apiman.yml`, `docs/apiman.md` y `postman/reservas-api-apiman.postman_environment.json`.
- AWS: `docs/aws.md`, `.env.example` y `scripts/aws-user-data.sh`.
- Seguridad JWT: `SecurityConfig`, `JwtService`, `JwtAuthenticationFilter` y `AuthController`.

## Endpoints versionados

La v1 se mantiene y la v2 se publica en:

```text
GET    /api/v2/clientes
POST   /api/v2/clientes
PUT    /api/v2/clientes/{id}
DELETE /api/v2/clientes/{id}
```

Cambios principales:

- Entrada con `ClienteV2RequestDTO`.
- Salida con `ClienteV2ResponseDTO`.
- `telefono` se expone como `telefonoContacto`.
- Se anade el campo calculado `nivel`.
- Se anade el campo calculado `deudaPendiente`.
- `DELETE` devuelve una confirmacion JSON con `DeleteResponseDTO`.

## Ejecucion local con Docker

Levantar API y base de datos:

```bash
docker compose up -d --build api
```

Servicios principales:

```text
API:        http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui/index.html
OpenAPI:    http://localhost:8080/v3/api-docs
MariaDB:    localhost:3306
```

Credenciales JWT de desarrollo/local:

```text
admin / admin123
```

## Pruebas

Tests Java:

```bash
mvn test
```

Resultado verificado:

```text
42 tests, 0 fallos, 0 errores
```

Tests Postman con Newman, con la API arrancada:

```bash
newman run postman/clientes-integration.postman_collection.json --env-var baseUrl=http://localhost:8080
newman run postman/all-entities-integration.postman_collection.json --env-var baseUrl=http://localhost:8080
```

Resultados verificados:

```text
Clientes:          9 peticiones, 19 comprobaciones, 0 fallos
Todas entidades:  38 peticiones, 71 comprobaciones, 0 fallos
```

Las colecciones hacen login automaticamente en:

```text
POST /api/auth/login
```

El token JWT se guarda y se reutiliza como Bearer Token.

## GitHub Actions

El workflow `.github/workflows/api-tests.yml`:

- Arranca una base PostgreSQL para pruebas.
- Ejecuta `mvn test`.
- Levanta la API.
- Instala Newman.
- Ejecuta las colecciones Postman.

Las ejecuciones se han comprobado en verde en GitHub Actions.

## APIMan

APIMan queda preparado mediante:

```text
docker-compose.apiman.yml
docs/apiman.md
postman/reservas-api-apiman.postman_environment.json
```

La configuracion documenta:

- API Manager y Developer Portal.
- Publicacion de la API en Gateway.
- Uso de `X-API-Key`.
- Politicas de limite de uso y cuota.
- Prueba desde Postman pasando por APIMan.

## AWS

El despliegue en AWS queda preparado con:

```text
Dockerfile
docker-compose.aws.yml
.env.example
scripts/aws-user-data.sh
docs/aws.md
```

Estado actual:

```text
Preparado, pero pendiente de prueba real porque AWS redirige a:
https://portal.aws.amazon.com/billing/signup/incomplete
```

Cuando AWS complete la activacion de la cuenta, se podra crear una instancia EC2 y ejecutar el procedimiento documentado en `docs/aws.md`.

## GitFlow

Ramas principales:

```text
main
develop
feature/*
```

Ramas feature usadas:

```text
feature/configuracion-entornos-mariadb
feature/versionado-endpoints
feature/postman-github-actions
feature/apiman-gateway
feature/aws-deployment
feature/documentacion-aa2
feature/jwt-security
feature/postman-all-entities
feature/update-github-actions
feature/documentacion-aws-pendiente
feature/informe-pdf-aa2
```

## Estructura

```text
docs/       Documentacion, informe PDF, AWS y APIMan
postman/    Colecciones y entornos Postman
scripts/    Scripts de datos y arranque AWS
src/        Codigo fuente y tests
wiremock/   Mock API de la entrega base
```
