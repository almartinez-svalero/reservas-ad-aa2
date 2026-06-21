# Despliegue en AWS

La propuesta de despliegue usa una instancia EC2 con Docker Compose. Es sencilla de defender porque ejecuta la misma API empaquetada en Docker junto con MariaDB en perfil `prod`.

## Servicios desplegados

```text
API Spring Boot: http://IP_PUBLICA_EC2:8080
Swagger UI:      http://IP_PUBLICA_EC2:8080/swagger-ui.html
APIMan UI:       http://IP_PUBLICA_EC2:8090/apimanui
APIMan Gateway:  http://IP_PUBLICA_EC2:8090/apiman-gateway
MariaDB:         solo dentro de Docker, sin exponer puerto publico
```

## 1. Crear la instancia EC2

Configuracion recomendada:

```text
AMI: Amazon Linux 2023
Tipo: t3.small o t3.medium
Disco: 20 GB
Security Group:
- SSH 22 solo desde tu IP
- HTTP 8080 desde tu IP o desde 0.0.0.0/0 si hay que ensenarlo
- HTTP 8090 desde tu IP o desde 0.0.0.0/0 si hay que ensenarlo
```

## 2. Usar el script de arranque

Al crear la instancia, pegar este contenido como User Data:

```text
scripts/aws-user-data.sh
```

El script instala Docker y Git, clona el repositorio y levanta:

```bash
docker compose -f docker-compose.aws.yml up -d --build
```

## 3. Configurar variables reales

El repositorio incluye:

```text
.env.example
```

En AWS hay que crear un `.env` real a partir de esa plantilla y cambiar las passwords:

```bash
cd /opt/reservas-api
cp .env.example .env
nano .env
docker compose -f docker-compose.aws.yml up -d --build
```

No se suben passwords reales al repositorio.

Tambien conviene cambiar las credenciales JWT:

```text
APP_SECURITY_USERNAME
APP_SECURITY_PASSWORD
APP_SECURITY_JWT_SECRET
```

## 4. Comprobaciones

API:

```bash
curl http://IP_PUBLICA_EC2:8080/api/clientes
```

Swagger:

```text
http://IP_PUBLICA_EC2:8080/swagger-ui.html
```

Contenedores:

```bash
docker ps
docker logs reservas-aws-api
```

## 5. APIMan en AWS

APIMan queda publicado en:

```text
http://IP_PUBLICA_EC2:8090/apimanui
```

Para publicar la API desde APIMan en AWS:

```text
Organizacion: reservas-aa2
API: reservas-api
Version: 1.0
Endpoint de implementacion: http://api:8080
Gateway publico: http://IP_PUBLICA_EC2:8090/apiman-gateway
```

Despues se crea el plan `plan-reservas`, se anaden las politicas de limite de uso y cuota, se crea una aplicacion cliente y se genera el API Key.

URL gestionada:

```text
http://IP_PUBLICA_EC2:8090/apiman-gateway/reservas-aa2/reservas-api/1.0/api/clientes
```
