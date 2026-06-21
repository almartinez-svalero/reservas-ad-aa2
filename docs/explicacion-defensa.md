# Guion rápido para defender el proyecto

## 1. Qué hace la aplicación

Este proyecto es una API REST para gestionar reservas de un restaurante. Permite dar de alta restaurantes, mesas, clientes, empleados y reservas. La idea es que un restaurante pueda controlar qué cliente reserva, en qué mesa, para qué día y qué empleado gestiona la reserva.

## 2. Por qué cumple el supuesto real

El supuesto es realista porque un restaurante necesita almacenar clientes, mesas disponibles, empleados responsables y reservas confirmadas o pendientes. Además, las entidades están relacionadas entre sí: una mesa pertenece a un restaurante, un empleado trabaja en un restaurante y una reserva une cliente, mesa y empleado.

## 3. Modelo de datos

Las clases están en:

```text
src/main/java/com/aa/reservas/model
```

Hay 5 clases:

- `Restaurante`
- `Mesa`
- `Cliente`
- `Empleado`
- `Reserva`

Cada una tiene más de 6 atributos. Por ejemplo, `Reserva` tiene código, fecha, hora, número de personas, importe de señal, confirmada, observaciones, cliente, mesa y empleado.

## 4. Relaciones

Las relaciones principales son:

- `Mesa` tiene una relación `@ManyToOne` con `Restaurante`.
- `Empleado` tiene una relación `@ManyToOne` con `Restaurante`.
- `Reserva` tiene relación `@ManyToOne` con `Cliente`, `Mesa` y `Empleado`.

Esto permite saber qué cliente hace la reserva, qué mesa se asigna y qué empleado la gestiona.

## 5. CRUD

Los controladores están en:

```text
src/main/java/com/aa/reservas/controller
```

Cada clase tiene:

- `GET` para listar
- `GET /{id}` para buscar uno
- `POST` para crear
- `PUT` para actualizar completo
- `PATCH` para modificar parcialmente
- `DELETE` para eliminar

## 6. Validaciones

Las validaciones están en las propias entidades mediante anotaciones. Por ejemplo:

- `@NotBlank` para campos obligatorios como nombre, email o código.
- `@Email` para correos.
- `@Pattern` para teléfonos.
- `@Min` para valores mínimos.
- `@PastOrPresent` para fechas que no pueden estar en el futuro.

## 7. Control de errores

El control global de errores está en:

```text
src/main/java/com/aa/reservas/exception/GlobalExceptionHandler.java
```

Ahí se controlan:

- 400 cuando hay errores de validación o petición incorrecta.
- 404 cuando no existe el recurso.
- 500 cuando ocurre un error interno.

## 8. Filtros

Cada recurso tiene un `GET` con hasta 3 filtros.

Ejemplos:

```text
GET /api/restaurantes?nombre=BJ&activo=true&aforoMinimo=20
GET /api/clientes?nombre=Laura&email=email.com&vip=true
GET /api/reservas?clienteId=1&fechaReserva=2026-01-15&confirmada=true
```

## 9. Endpoints extra con DTOs

Se han añadido endpoints que no son CRUD básico:

```text
GET /api/restaurantes/{id}/resumen
GET /api/clientes/{id}/historial
POST /api/reservas/asignar
```

El más importante para explicar es `POST /api/reservas/asignar`, porque recibe un DTO con los ids de cliente, mesa y empleado. El servicio busca esas entidades en base de datos, valida que existan y crea la reserva relacionándolas.

## 10. JPQL y SQL nativo

Las consultas JPQL y SQL nativas están en los repositorios:

```text
src/main/java/com/aa/reservas/repository
```

JPQL:

- Clientes VIP con reservas.
- Mesas disponibles por restaurante y capacidad.
- Reservas confirmadas por restaurante.

SQL nativo:

- Restaurantes activos.
- Empleados por restaurante.
- Reservas por fecha.

## 11. Tests

Los tests están en:

```text
src/test/java/com/aa/reservas
```

Hay tests de Service con Mockito y tests de Controller con MockMvc. Se prueban casos de éxito, errores 400 y errores 404.

## 12. OpenAPI, Postman y WireMock

- OpenAPI: `docs/openapi.yaml`
- Colección Postman: `postman/reservas-api.postman_collection.json`
- Entorno Postman: `postman/reservas-api.postman_environment.json`
- WireMock: `wiremock/mappings`

## 13. Frase útil para defender

“Este proyecto no se limita a guardar datos sueltos. Lo importante es que la reserva se crea relacionando cliente, mesa y empleado, y además se valida que la mesa exista, sea reservable y tenga capacidad suficiente para el número de personas.”
