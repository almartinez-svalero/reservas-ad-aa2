# Reservas API - AA1 Acceso a Datos

## AA2 - Testing y Gestion de APIs

Esta version amplia la entrega inicial para la Actividad de Aprendizaje de 2 Evaluacion.

Repositorio GitHub:

```text
https://github.com/almartinez-svalero/reservas-ad-aa2
```

Evidencias principales:

- Versionado de endpoints en `GET`, `POST`, `PUT` y `DELETE`: `src/main/java/com/aa/reservas/controller/ClienteV2Controller.java`.
- Configuracion por entornos: `application-dev.properties` y `application-prod.properties`.
- Produccion con MariaDB: `pom.xml`, `docker-compose.yml` y `docker-compose.aws.yml`.
- Tests de integracion Postman: `postman/clientes-integration.postman_collection.json`.
- Tests Postman completos para todas las entidades: `postman/all-entities-integration.postman_collection.json`.
- Automatizacion GitHub Actions: `.github/workflows/api-tests.yml`.
- APIMan local y gateway con token: `docker-compose.apiman.yml` y `docs/apiman.md`.
- Despliegue AWS: `docs/aws.md` y `scripts/aws-user-data.sh`.
- Estado AWS: despliegue preparado; prueba real pendiente hasta que AWS complete la activacion de cuenta y deje acceder a EC2.
- Seguridad JWT: `src/main/java/com/aa/reservas/config/SecurityConfig.java` y `src/main/java/com/aa/reservas/controller/AuthController.java`.
- Checklist completa: `docs/checklist-aa2.md`.

### Endpoints versionados

La v1 se mantiene sin romper compatibilidad. La v2 se publica en:

```text
GET    /api/v2/clientes
POST   /api/v2/clientes
PUT    /api/v2/clientes/{id}
DELETE /api/v2/clientes/{id}
```

Cambios principales de v2:

- Entrada mediante `ClienteV2RequestDTO`.
- Salida mediante `ClienteV2ResponseDTO`.
- `telefono` pasa a exponerse como `telefonoContacto`.
- Se anade el campo calculado `nivel`: `VIP` o `ESTANDAR`.
- Se anade el campo calculado `deudaPendiente`.
- `DELETE` devuelve una confirmacion JSON.

### Ejecutar tests de AA2

Tests Java:

```bash
mvn test
```

Tests Postman con Newman, con la API arrancada en local:

```bash
newman run postman/clientes-integration.postman_collection.json --env-var baseUrl=http://localhost:8080
newman run postman/all-entities-integration.postman_collection.json --env-var baseUrl=http://localhost:8080
```

La coleccion hace login automaticamente contra:

```text
POST /api/auth/login
```

Credenciales por defecto de desarrollo:

```text
admin / admin123
```

Proyecto de API REST con **Spring Boot**, **JPA/Hibernate** y **PostgreSQL** para gestionar reservas de restaurante.

El supuesto real elegido es una aplicación interna para que un restaurante gestione clientes, mesas, empleados y reservas.

## 1. Tecnologías usadas

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven
- OpenAPI 3.0
- Postman
- WireMock
- JUnit 5 + Mockito + MockMvc

## 2. Modelo de datos

El modelo tiene 5 clases principales:

| Clase | Descripción | Relaciones |
|---|---|---|
| `Restaurante` | Local donde se realizan las reservas | Tiene mesas y empleados asociados |
| `Mesa` | Mesa física del restaurante | Pertenece a un restaurante |
| `Cliente` | Persona que realiza reservas | Puede aparecer en varias reservas |
| `Empleado` | Trabajador que gestiona reservas | Pertenece a un restaurante |
| `Reserva` | Reserva concreta para fecha y hora | Relaciona cliente, mesa y empleado |

Cada clase tiene al menos 6 atributos y validaciones con anotaciones como `@NotBlank`, `@Email`, `@Pattern`, `@Min`, `@DecimalMin`, `@PastOrPresent` o `@NotNull`.

## 3. Requisitos obligatorios cubiertos

### 3.1 OpenAPI 3.0 y WireMock

- Especificación OpenAPI: `docs/openapi.yaml`
- Mock API con WireMock: carpeta `wiremock/mappings`

La especificación incluye respuestas 20X, 400, 404 y 500 en las operaciones principales.

### 3.2 Tests unitarios

Hay tests para la capa Service de las 5 clases principales:

- `RestauranteServiceTest`
- `ClienteServiceTest`
- `MesaServiceTest`
- `EmpleadoServiceTest`
- `ReservaServiceTest`

También hay tests de Controller con casos 20X, 400 y 404 usando MockMvc.

### 3.3 CRUD completo

Cada entidad tiene operaciones:

- `GET /api/{recurso}`
- `GET /api/{recurso}/{id}`
- `POST /api/{recurso}`
- `PUT /api/{recurso}/{id}`
- `PATCH /api/{recurso}/{id}`
- `DELETE /api/{recurso}/{id}`

### 3.4 Control de errores

Los errores se gestionan en:

```text
src/main/java/com/aa/reservas/exception/GlobalExceptionHandler.java
```

Se controlan:

- 400: validaciones y peticiones incorrectas
- 404: recurso no encontrado
- 500: error interno no controlado

### 3.5 Filtros GET

Cada clase tiene un endpoint GET con hasta 3 filtros:

| Recurso | Filtros |
|---|---|
| Restaurantes | `nombre`, `activo`, `aforoMinimo` |
| Clientes | `nombre`, `email`, `vip` |
| Mesas | `restauranteId`, `capacidadMinima`, `reservable` |
| Empleados | `restauranteId`, `puesto`, `activo` |
| Reservas | `clienteId`, `fechaReserva`, `confirmada` |

### 3.6 Colección Postman

La colección está en:

```text
postman/reservas-api.postman_collection.json
```

Y el entorno parametrizado en:

```text
postman/reservas-api.postman_environment.json
```

## 4. Funcionalidades extra incluidas

Para aproximarse a una nota alta sin sobrecomplicar el proyecto, se han añadido varias funcionalidades extra:

### Extra 1: PATCH en todas las clases

Todas las entidades tienen operación `PATCH`, permitiendo modificar cualquier atributo de forma parcial.

### Extra 2: Endpoints con DTOs y relaciones

- `GET /api/restaurantes/{id}/resumen`
- `GET /api/clientes/{id}/historial`
- `POST /api/reservas/asignar`

Estos endpoints usan DTOs y aprovechan relaciones entre entidades.

### Extra 3: Consultas JPQL

- `GET /api/clientes/jpql/vip-con-reservas`
- `GET /api/mesas/jpql/disponibles?restauranteId=1&capacidadMinima=2`
- `GET /api/reservas/jpql/confirmadas-por-restaurante/1`

### Extra 4: Consultas SQL nativas

- `GET /api/restaurantes/native/activos`
- `GET /api/empleados/native/por-restaurante/1`
- `GET /api/reservas/native/por-dia?fecha=2026-01-15`

### Extra 5: Logs de operaciones y errores

Se ha añadido un aspecto de logging en:

```text
src/main/java/com/aa/reservas/config/LoggingAspect.java
```

También se guarda log en:

```text
logs/reservas-api.log
```

## 5. Cómo arrancar el proyecto

### Paso 1: levantar PostgreSQL

Desde la raíz del proyecto:

```bash
docker compose up -d
```

Esto crea una base de datos PostgreSQL con estos datos:

```text
Base de datos: reservas_db
Usuario: reservas_user
Contraseña: reservas_pass
Puerto: 5432
```

### Paso 2: arrancar Spring Boot

```bash
mvn spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

### Paso 3: cargar datos de ejemplo

Cuando la aplicación haya creado las tablas, se puede ejecutar el script:

```text
scripts/datos-demo.sql
```

En pgAdmin, DBeaver o desde terminal.

## 6. Ejecutar tests

```bash
mvn test
```

## 7. Probar con Postman

1. Importar `postman/reservas-api.postman_collection.json`.
2. Importar `postman/reservas-api.postman_environment.json`.
3. Seleccionar el entorno `Reservas API Local`.
4. Ejecutar primero:
   - crear restaurante
   - crear cliente
   - crear mesa
   - crear empleado
   - crear reserva

Después ya se pueden probar filtros, PATCH, DELETE y endpoints extra.

## 8. Probar WireMock

```bash
docker run --rm -it -p 8089:8080 -v ${PWD}/wiremock:/home/wiremock wiremock/wiremock:3.9.1
```

Ejemplos:

```bash
curl http://localhost:8089/api/restaurantes
curl http://localhost:8089/api/clientes
curl http://localhost:8089/api/reservas
```

## 9. Flujo Git recomendado

Aunque el repositorio remoto debe crearse manualmente en GitHub, el proyecto está preparado para trabajar con Git Flow:

```bash
git init
git add .
git commit -m "chore: initial spring boot project"
git checkout -b develop
git checkout -b feature/modelo-datos
git checkout -b feature/crud-endpoints
git checkout -b feature/tests
git checkout -b feature/openapi-postman-wiremock
git checkout -b feature/extras-jpql-native-logs
```

Posibles issues para GitHub:

- Crear modelo de datos y relaciones.
- Implementar CRUD de restaurantes.
- Implementar CRUD de clientes.
- Implementar CRUD de mesas.
- Implementar CRUD de empleados.
- Implementar CRUD de reservas.
- Añadir filtros GET.
- Añadir tests de Service.
- Añadir tests de Controller.
- Añadir OpenAPI y colección Postman.
- Añadir WireMock.
- Añadir endpoints extra con DTO, JPQL y SQL nativo.

## 10. Estructura del proyecto

```text
reservas-api-aa1/
├── docs/
│   └── openapi.yaml
├── postman/
│   ├── reservas-api.postman_collection.json
│   └── reservas-api.postman_environment.json
├── scripts/
│   └── datos-demo.sql
├── wiremock/
│   └── mappings/
├── src/
│   ├── main/java/com/aa/reservas/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── test/java/com/aa/reservas/
├── docker-compose.yml
├── pom.xml
└── README.md
```
