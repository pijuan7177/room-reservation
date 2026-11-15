# room-reservation

API REST para la gestión de salas y reservas, construida con Spring Boot 4.1.2 y Java 25.

El proyecto permite crear salas, consultar su listado, crear reservas comprobando que la sala
no esté ocupada, consultar reservas con filtros, paginación y ordenación, y cancelar reservas.
Los endpoints de escritura están protegidos con JWT; las consultas son públicas. El esquema de
base de datos se gestiona con Liquibase y la API se documenta con Swagger/OpenAPI.

## Funcionalidades

- Gestión de salas:
  - Creación de salas con validación de nombre, descripción, capacidad y tamaño.
  - Listado de todas las salas.
  - Restricción de nombre de sala único.
- Gestión de reservas:
  - Creación de reservas con validación de fechas futuras y existencia de la sala.
  - Detección de solapamientos por sala. Se permiten reservas adyacentes y las reservas
    canceladas no bloquean nuevas reservas.
  - Consulta paginada con filtros por nombre de sala y rango de fechas.
  - Ordenación por nombre de sala, fecha de inicio o fecha de fin.
  - Cancelación de reservas.
- Seguridad:
  - Autenticación mediante `POST /auth/login`.
  - Tokens JWT firmados con HMAC-SHA256.
  - Endpoints de escritura protegidos con `Authorization: Bearer <token>`.
- Documentación interactiva con Swagger UI y esquema OpenAPI.
- Migraciones de base de datos con Liquibase.
- Tests unitarios y de integración para la creación de salas.

## Stack

- Java 25
- Spring Boot 4.1.2
- Spring Web MVC, Spring Data JPA e Hibernate
- Spring Security con OAuth2 Resource Server (JWT)
- PostgreSQL
- Liquibase
- Springdoc OpenAPI / Swagger UI
- Lombok
- JUnit, Mockito y MockMvc

## Estructura del proyecto

```text
src/main/java/com/rreserv/room/reservation/
  config/       Configuración de OpenAPI
  model/        Entidades JPA
  repository/   Repositorios Spring Data JPA y consultas con Criteria
  rest/         Controladores, DTOs de entrada/salida y convertidores
  security/     Configuración de Spring Security y emisión de JWT
  service/      Lógica de negocio, DTOs y convertidores
src/main/resources/
  application.properties
  db/changelog/ Migraciones Liquibase
```

## Requisitos

- JDK 25.
- PostgreSQL accesible en `localhost:5432`.
- Base de datos `rreserv` creada. Liquibase crea las tablas automáticamente al arrancar.
- Para ejecutar los tests de integración, base de datos `rreserv_test`.

## Ejecución

El proyecto incluye Maven Wrapper. Desde la raíz del proyecto:

```powershell
# Windows PowerShell
.\mvnw.cmd spring-boot:run
```

```bash
# Linux / macOS
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

Comandos útiles:

```powershell
# Ejecutar todos los tests
.\mvnw.cmd test

# Ejecutar una clase de tests
.\mvnw.cmd -Dtest=RoomServiceImplTests test
.\mvnw.cmd -Dtest=RoomControllerIntegrationTests test

# Empaquetar la aplicación
.\mvnw.cmd package
```

Los tests de integración usan el perfil `test`, la base de datos `rreserv_test` y el changelog
`src/test/resources/db/changelog/liquibase-changelog-test.xml`. La suite actual incluye:

- `RoomServiceImplTests`: tests unitarios de la creación de salas con Mockito.
- `RoomControllerIntegrationTests`: tests de integración con MockMvc que cubren el login JWT,
  la creación de salas, la validación del payload, el conflicto por nombre duplicado y los
  casos `401` por token ausente o inválido.

## Configuración de seguridad

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `APP_SECURITY_USERNAME` | `admin` | Usuario para `POST /auth/login` |
| `APP_SECURITY_PASSWORD` | `admin` | Contraseña para `POST /auth/login` |
| `JWT_SECRET` | Secreto de desarrollo del proyecto | Clave HMAC-SHA256; debe tener al menos 32 bytes |
| `JWT_EXPIRATION` | `PT1H` | Duración del token en formato ISO-8601 |

> **Importante:** los valores por defecto son solo para desarrollo. En cualquier entorno real
> deben sobrescribirse `APP_SECURITY_USERNAME`, `APP_SECURITY_PASSWORD` y `JWT_SECRET`.

Las rutas públicas son:

- `POST /auth/login`
- `GET /rooms`
- `GET /reservations`
- Swagger UI y OpenAPI (`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`)
- `/actuator/health`

El resto de rutas de la API requiere un token JWT válido. Por defecto, el token caduca una hora
después de su emisión.

## Endpoints

| Método | Ruta | Autenticación | Descripción |
|---|---|---|---|
| `POST` | `/auth/login` | Pública | Obtiene un token JWT |
| `GET` | `/rooms` | Pública | Lista todas las salas |
| `POST` | `/rooms` | JWT | Crea una sala |
| `GET` | `/reservations` | Pública | Consulta reservas con filtros y paginación |
| `POST` | `/reservations` | JWT | Crea una reserva |
| `PATCH` | `/reservations/{reservationId}/cancel` | JWT | Cancela una reserva |

### `POST /auth/login`

```json
{
  "username": "admin",
  "password": "admin"
}
```

Respuesta `200 OK`:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600
}
```

Devuelve `401` si las credenciales son incorrectas.

### `GET /rooms`

Respuesta `200 OK`:

```json
{
  "total": 1,
  "results": [
    {
      "id": "9b0f7d0e-6e0a-4f2e-9f1a-5a4d2c3b1a00",
      "name": "Sala Norte",
      "description": "Sala con proyector",
      "capacity": 12,
      "size": 35.5
    }
  ]
}
```

### `POST /rooms`

Requiere JWT.

```json
{
  "name": "Sala Norte",
  "description": "Sala con proyector",
  "capacity": 12,
  "size": 35.5
}
```

- `name`: obligatorio, máximo 30 caracteres, único.
- `description`: obligatoria, máximo 500 caracteres.
- `capacity`: obligatoria y positiva.
- `size`: obligatorio y positivo.

Respuesta `201 Created` con cabecera `Location: /rooms/{id}` y cuerpo:

```json
{
  "id": "9b0f7d0e-6e0a-4f2e-9f1a-5a4d2c3b1a00"
}
```

Códigos posibles: `201`, `400` (validación), `401` (sin token o token inválido), `409` (nombre
de sala duplicado).

### `GET /reservations`

Parámetros de consulta:

| Parámetro | Tipo | Valor por defecto | Descripción |
|---|---|---|---|
| `room_name` | string | — | Búsqueda parcial por nombre de sala, sin distinguir mayúsculas |
| `start_date` | ISO-8601 Instant | — | Reservas cuya fecha de inicio sea mayor o igual |
| `end_date` | ISO-8601 Instant | — | Reservas cuya fecha de fin sea menor o igual |
| `offset` | entero | `0` | Desplazamiento, mayor o igual que 0 |
| `limit` | entero | `10` | Tamaño de página, entre 10 y 100 |
| `sort_by` | enum | `START_DATE` | `ROOM_NAME`, `START_DATE` o `END_DATE` |
| `sort_dir` | enum | `DESC` | `ASC` o `DESC` |

Ejemplo:

```text
GET /reservations?room_name=norte&start_date=2026-12-01T00:00:00Z&limit=10&sort_by=START_DATE&sort_dir=ASC
```

Respuesta `200 OK`:

```json
{
  "total": 1,
  "results": [
    {
      "id": "e4d3c2b1-a0f9-4e8d-9c7b-6a5f4e3d2c1b",
      "roomName": "Sala Norte",
      "customerName": "Ana",
      "startDate": "2026-12-01T10:00:00Z",
      "endDate": "2026-12-01T11:00:00Z",
      "canceled": false
    }
  ]
}
```

### `POST /reservations`

Requiere JWT.

```json
{
  "roomId": "9b0f7d0e-6e0a-4f2e-9f1a-5a4d2c3b1a00",
  "customerName": "Ana",
  "startDate": "2026-12-01T10:00:00Z",
  "endDate": "2026-12-01T11:00:00Z"
}
```

- `roomId`: UUID de una sala existente.
- `customerName`: obligatorio, máximo 50 caracteres.
- `startDate` y `endDate`: obligatorios, en el futuro y en formato ISO-8601.

Respuesta `201 Created` con cabecera `Location: /reservations/{id}` y cuerpo:

```json
{
  "id": "e4d3c2b1-a0f9-4e8d-9c7b-6a5f4e3d2c1b"
}
```

Códigos posibles: `201`, `400` (validación), `401` (sin token o token inválido), `404`
(sala inexistente), `409` (solapamiento con otra reserva de la misma sala).

La comprobación de solapamiento usa intervalos semiabiertos:

```text
existing.startDate < requested.endDate AND existing.endDate > requested.startDate
```

Las reservas adyacentes están permitidas y las canceladas no se tienen en cuenta.

### `PATCH /reservations/{reservationId}/cancel`

Requiere JWT.

- `204 No Content`: reserva cancelada.
- `404 Not Found`: reserva inexistente.
- `401 Unauthorized`: sin token o token inválido.

## Uso de Swagger y del token JWT

1. Arranca la aplicación.
2. Abre `http://localhost:8080/swagger-ui.html`.
3. Ejecuta `POST /auth/login`:
   - Pulsa **Try it out**.
   - Usa las credenciales configuradas, por defecto:
     ```json
     {
       "username": "admin",
       "password": "admin"
     }
     ```
   - Pulsa **Execute**.
4. Copia el valor de `accessToken` de la respuesta. No copies las comillas.
5. Pulsa el botón **Authorize** de Swagger UI.
6. Pega únicamente el token en el campo de valor, **sin** el prefijo `Bearer `.
7. Confirma con **Authorize** y cierra el diálogo.

A partir de ese momento Swagger enviará automáticamente:

```http
Authorization: Bearer <accessToken>
```

en los endpoints protegidos: `POST /rooms`, `POST /reservations` y
`PATCH /reservations/{reservationId}/cancel`.

Los endpoints públicos (`GET /rooms` y `GET /reservations`) funcionan sin autorización. Si el
token caduca, hay que repetir el login y volver a pulsar **Authorize**.

Swagger UI no obtiene el token automáticamente al introducir usuario y contraseña: el login se
hace con `POST /auth/login` y el token se pega manualmente en **Authorize**. Para automatizarlo
haría falta un flujo OAuth2 de tipo password o un plugin personalizado de Swagger UI.

### Ejemplo con curl

```bash
# Obtener el token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Crear una sala usando el token
curl -X POST http://localhost:8080/rooms \
  -H "Authorization: Bearer <accessToken>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Sala Norte","description":"Sala con proyector","capacity":12,"size":35.5}'

# Consultar reservas (endpoint público)
curl "http://localhost:8080/reservations?limit=10&sort_by=START_DATE&sort_dir=ASC"
```

El esquema OpenAPI sin procesar está disponible en `http://localhost:8080/v3/api-docs`.

## Mejoras posibles

### Seguridad

- Sustituir el usuario en memoria por usuarios persistidos en base de datos, LDAP u OIDC.
- Añadir roles y autorización por propietario de la reserva (por ejemplo, solo el creador puede
  cancelarla).
- Eliminar las credenciales y el secreto por defecto, o moverlos a un perfil `dev` y hacer que
  la aplicación falle al arrancar si faltan en otros entornos.
- Añadir refresh tokens, revocación de tokens y rotación de la clave de firma.
- Incorporar limitación de intentos de login y protección contra fuerza bruta.
- Servir la API exclusivamente por HTTPS y configurar CORS si se consume desde un frontend.

### Dominio y API

- Capturar la violación de unicidad de la base de datos al crear salas y devolver `409` también
  en ese caso.
- Gestionar los valores inválidos de `sort_by` y `sort_dir` en `GET /reservations` para devolver
  `400` en lugar de propagar una excepción no controlada.
- Incorporar un estado de reserva (`ACTIVE`, `CANCELED`, etc.) en lugar de un booleano y definir
  reglas para cancelar reservas pasadas o ya canceladas.
- Añadir versionado de API e idempotencia en la creación de recursos.

### Tests e infraestructura

- Sustituir la dependencia de PostgreSQL local en tests por Testcontainers o una base embebida.
- Separar configuraciones por perfiles (`dev`, `test`, `prod`) y externalizar secretos.
- Añadir observabilidad: logs estructurados, métricas, trazas y health checks más completos.

### Documentación

- Documentar ejemplos y códigos de error directamente en OpenAPI.
- Implementar autenticación automática en Swagger mediante un flujo OAuth2 password.
- Generar documentación de API con Spring REST Docs a partir de los tests.
