# API Mock con WireMock

Esta carpeta contiene mappings para simular la API sin arrancar Spring Boot.

Comando recomendado:

```bash
docker run --rm -it -p 8089:8080 -v ${PWD}/wiremock:/home/wiremock wiremock/wiremock:3.9.1
```

Después se puede probar, por ejemplo:

```bash
curl http://localhost:8089/api/restaurantes
curl http://localhost:8089/api/clientes
curl http://localhost:8089/api/reservas
```
