# Checklist AA2 - Testing y Gestion de APIs

## Requisitos obligatorios

- [x] Versionado de 4 endpoints: `GET`, `POST`, `PUT` y `DELETE` en `/api/v2/clientes`.
- [x] Cambios explicables en entrada/salida con `ClienteV2RequestDTO`, `ClienteV2ResponseDTO` y `DeleteResponseDTO`.
- [x] Configuracion externalizada con `application-dev.properties` y `application-prod.properties`.
- [x] Produccion configurada con MariaDB.
- [x] Despliegue AWS preparado con `docker-compose.aws.yml`, `.env.example` y `scripts/aws-user-data.sh`.
- [ ] Prueba real en EC2 pendiente de activacion completa de la cuenta AWS. Actualmente AWS redirige a `https://portal.aws.amazon.com/billing/signup/incomplete`.
- [x] Tests de integracion Postman para la entidad Cliente.
- [x] Tests de integracion Postman ampliados para Restaurante, Cliente, Mesa, Empleado y Reserva.
- [x] GitHub Actions con Maven, PostgreSQL, API arrancada y Newman.
- [x] APIMan preparado con Docker Compose, entorno Postman, token `X-API-Key` y politicas de limite de uso.
- [x] API securizada con JWT.
- [x] Postman obtiene automaticamente el JWT y lo reutiliza en las peticiones.
- [x] Informe explicativo de requisitos incluido en `docs/Informe-AA2-Requisitos.pdf`.

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
- `feature/postman-all-entities`
- `feature/update-github-actions`
- `feature/documentacion-aws-pendiente`
- `feature/informe-pdf-aa2`

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

## Estado AWS

La configuracion para AWS esta incluida en el repositorio y validada a nivel de Docker Compose. La ejecucion real queda pendiente porque la cuenta de AWS no permite todavia acceder a EC2 y redirige al proceso de alta incompleta de billing.

Cuando AWS active la cuenta, se seguira el procedimiento de `docs/aws.md` para lanzar EC2, levantar la API, MariaDB y APIMan, y ejecutar las pruebas contra la IP publica.
