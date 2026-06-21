# Despliegue en AWS

La propuesta de despliegue usa una instancia EC2 con Docker Compose. Es sencilla de defender porque ejecuta la misma API empaquetada en Docker junto con MariaDB en perfil `prod`.

## Estado de la prueba real

El despliegue en AWS queda preparado y documentado, pero la prueba real en EC2 queda pendiente hasta que AWS permita utilizar los servicios de la cuenta.

Durante la comprobacion, la consola redirige al alta incompleta de cuenta:

```text
https://portal.aws.amazon.com/billing/signup/incomplete
```

Por este motivo no se ha podido crear todavia la instancia EC2 ni obtener una IP publica de prueba. En cuanto AWS complete la activacion de la cuenta y deje acceder a EC2, se puede ejecutar el procedimiento de este documento sin modificar el codigo de la aplicacion.

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

Pasos cuando AWS active la cuenta:

```text
1. Entrar en EC2 en la region eu-west-3.
2. Crear una instancia Amazon Linux 2023.
3. Seleccionar un tipo compatible con la capa gratuita si esta disponible.
4. Crear o reutilizar un par de claves SSH.
5. Configurar el Security Group con los puertos 22, 8080 y 8090.
6. Pegar el contenido de scripts/aws-user-data.sh en User Data.
7. Lanzar la instancia y esperar a que Docker Compose termine de levantar los servicios.
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

Prueba esperada:

```text
1. La API responde en http://IP_PUBLICA_EC2:8080/v3/api-docs.
2. Swagger abre en http://IP_PUBLICA_EC2:8080/swagger-ui.html.
3. APIMan abre en http://IP_PUBLICA_EC2:8090/apimanui.
4. La coleccion Postman puede ejecutarse cambiando baseUrl por la IP publica.
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
