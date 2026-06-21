# APIMan - Publicacion de la API

Esta guia deja preparada la parte de API Manager + Developer Portal de la actividad.

## 1. Arrancar la API

Primero levantar la API y la base de datos de produccion:

```bash
docker compose up -d mariadb api
```

La API queda disponible en:

```text
http://localhost:8080
```

## 2. Arrancar APIMan

En otra terminal, desde la raiz del proyecto:

```bash
docker compose -f docker-compose.apiman.yml up -d
```

Accesos locales:

```text
APIMan UI: http://localhost:8090/apimanui
Gateway:   http://localhost:8090/apiman-gateway
```

Usuario por defecto de la imagen:

```text
Usuario: admin
Password: admin123!
```

## 3. Crear organizacion, API y version

1. Crear una organizacion llamada `reservas-aa2`.
2. Crear una API llamada `reservas-api`.
3. Crear la version `1.0`.
4. Configurar el endpoint de implementacion como:

```text
http://host.docker.internal:8080
```

Ese endpoint permite que el contenedor de APIMan llame a la API que esta publicada en el host.

## 4. Configurar token y politicas

Para que el uso de la API requiera token:

1. Crear un plan llamado `plan-reservas`.
2. Anadir el plan a la API.
3. Crear una aplicacion cliente llamada `postman-client`.
4. Crear un contrato entre `postman-client`, `reservas-api` y `plan-reservas`.
5. Copiar el API Key generado por APIMan.

Politicas propuestas para el plan:

```text
Rate Limiting: 10 peticiones por minuto.
Quota: 1000 peticiones al mes.
```

Estas dos politicas afectan al funcionamiento de la API porque limitan el uso por token.

## 5. Probar desde Postman pasando por APIMan

Importar:

```text
postman/clientes-integration.postman_collection.json
postman/reservas-api-apiman.postman_environment.json
```

En el entorno `Reservas API - APIMan Local`, sustituir:

```text
PEGAR_AQUI_EL_API_KEY_DE_APIMAN
```

por el API Key generado por el contrato de APIMan.

La coleccion envia el token en la cabecera:

```text
X-API-Key: {{apiKey}}
```

URL gestionada esperada:

```text
http://localhost:8090/apiman-gateway/reservas-aa2/reservas-api/1.0/api/clientes
```
