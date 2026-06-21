# Checklist AA2 - Testing y Gestion de APIs

## Requisitos obligatorios

- [x] Versionado de 4 endpoints: `GET`, `POST`, `PUT` y `DELETE` en `/api/v2/clientes`.
- [x] Cambios explicables en entrada/salida con `ClienteV2RequestDTO`, `ClienteV2ResponseDTO` y `DeleteResponseDTO`.
- [x] Configuracion externalizada con `application-dev.properties` y `application-prod.properties`.
- [x] Produccion configurada con MariaDB.
- [x] Despliegue AWS preparado con `docker-compose.aws.yml`, `.env.example` y `scripts/aws-user-data.sh`.
- [x] Tests de integracion Postman para la entidad Cliente.
- [x] GitHub Actions con Maven, PostgreSQL, API arrancada y Newman.
- [x] APIMan preparado con Docker Compose, entorno Postman, token `X-API-Key` y politicas de limite de uso.

## Git Flow

Ramas usadas:

- `main`
- `develop`
- `feature/configuracion-entornos-mariadb`
- `feature/versionado-endpoints`
- `feature/postman-github-actions`
- `feature/apiman-gateway`
- `feature/aws-deployment`
- `feature/documentacion-aa2`

Repositorio remoto:

```text
https://github.com/almartinez-svalero/reservas-ad-aa2
```

## Verificacion local

```bash
mvn test
docker compose -f docker-compose.apiman.yml config
docker compose --env-file .env.example -f docker-compose.aws.yml config
```
