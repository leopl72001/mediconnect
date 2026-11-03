# MediConnect — API de gestión de citas médicas

Proyecto universitario construido desde cero con **Java 21, Spring Boot 3.5.7 y Maven**. Se desarrolla en un repositorio independiente del sistema anterior.

## Funcionalidad implementada

- Registro de pacientes, autenticación **JWT HS512**, contraseñas BCrypt y roles PATIENT/DOCTOR/ADMIN.
- Administración de médicos y consultorios; catálogo público de especialidades.
- Reserva de citas con bloqueo pesimista, validación de solapamientos (HTTP 409) y cancelación con más de 24 horas de anticipación.
- Spring Security stateless, `@PreAuthorize`, DTOs con records, ProblemDetail, Swagger, Actuator y virtual threads.
- Perfil **dev** con H2 en memoria, diseñado para arrancar sin Docker, MongoDB ni Redis.
- Tests de salud, autenticación y reglas de citas; CI con GitHub Actions.
- Docker Compose preparado para PostgreSQL, MongoDB y Redis.

**Estado:** esta versión es una base funcional de la entrega. No están implementados todavía los módulos de notas clínicas en MongoDB, prescripciones OpenFDA, caché Redis ni las migraciones para producción. Consulta `docs/plan-de-entrega.md`.

## Arranque local en Windows

Requisitos: JDK 21, Maven 3.9+, Git.

```powershell
git clone https://github.com/leopl72001/mediconnect.git
cd mediconnect
mvn test
mvn spring-boot:run
```

La aplicación usa el puerto **8081** para no chocar con el proyecto anterior.

- Salud: http://localhost:8081/actuator/health
- Swagger: http://localhost:8081/swagger-ui.html
- Especialidades: http://localhost:8081/api/v1/specialties

En el perfil dev, el endpoint health debe devolver `"status":"UP"`. El comando `mvn spring-boot:run` permanece activo mientras el servidor está funcionando.

## Registro y login

```powershell
$body = @{email='paciente@example.com';password='Password123!';firstName='Ana';lastName='Lopez'} | ConvertTo-Json
Invoke-RestMethod -Uri http://localhost:8081/api/v1/auth/register -Method Post -ContentType 'application/json' -Body $body
$login = @{email='paciente@example.com';password='Password123!'} | ConvertTo-Json
Invoke-RestMethod -Uri http://localhost:8081/api/v1/auth/login -Method Post -ContentType 'application/json' -Body $login
```

Para un administrador **solo en desarrollo**, define `DEV_ADMIN_EMAIL` y `DEV_ADMIN_PASSWORD` antes de iniciar el servidor. No existe registro público de administradores.

## Infraestructura Docker (opcional)

```powershell
Copy-Item .env.example .env
# Edita las credenciales de .env; nunca subas .env al repositorio.
docker compose up -d
docker compose ps
```

Puertos: PostgreSQL 5433, MongoDB 27018 y Redis 6380, todos enlazados a localhost. **No cambies a perfil prod todavía:** requiere migraciones y pruebas de integración.

## Seguridad

El JWT usa HS512 con un secreto de al menos 64 bytes. El valor de desarrollo es exclusivamente local. En producción se debe definir `JWT_SECRET` y proteger las credenciales.

## Verificación

La ejecución de `mvn test` en GitHub Actions es la fuente de verdad para saber si el proyecto compila y pasan sus pruebas. No se afirma que esta versión haya sido probada contra los tres contenedores.