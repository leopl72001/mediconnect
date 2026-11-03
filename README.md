# MediConnect — Gestión de citas médicas

**Proyecto universitario nuevo**, independiente del repositorio anterior. Backend REST con Java 21, Spring Boot 3.5.7, Maven, Spring Security, PostgreSQL, MongoDB y Redis.

## Características

- Registro de pacientes y login con **JWT HS512**, BCrypt y roles PATIENT, DOCTOR y ADMIN.
- Perfil de paciente (consulta y actualización de fecha de nacimiento, teléfono y dirección), especialidades, médicos, consultorios y administración protegida.
- Citas transaccionales con bloqueo pesimista del médico, rechazo de solapamientos **409 Conflict**, cancelación con más de 24 h.
- Notas clínicas en MongoDB: solo escribe el médico asignado; el paciente consulta exclusivamente las suyas; auditoría de autor y fecha.
- Prescripciones con consulta a OpenFDA, timeout de 1.5 s, Resilience4j Circuit Breaker y fallback `MANUAL_VALIDATION_REQUIRED`.
- Redis con caché de médicos (TTL 10 minutos) y medicamentos validados (TTL 24 horas).
- DTOs con records, MapStruct, ProblemDetail, Swagger/OpenAPI, Actuator, métricas y virtual threads.
- Docker Compose, Postman, pruebas JUnit/Mockito y CI GitHub Actions.

## Opción A: ejecutar sin Docker (DEV)

Requisitos: **JDK 21, Maven 3.9+** y Git.

```powershell
git clone https://github.com/leopl72001/mediconnect.git
cd mediconnect
mvn test
mvn spring-boot:run
```

En `dev`, el servidor usa H2 en memoria y desactiva MongoDB/Redis. Los endpoints de notas clínicas solo están habilitados en los perfiles `docker` y `prod`.

- Health: http://localhost:8081/actuator/health
- Swagger: http://localhost:8081/swagger-ui.html
- Especialidades: http://localhost:8081/api/v1/specialties

`mvn spring-boot:run` permanece activo mientras la API está ejecutándose. Para detenerla, Ctrl+C.

## Opción B: ejecutar todo con Docker Desktop (perfil docker)

```powershell
git clone https://github.com/leopl72001/mediconnect.git
cd mediconnect
Copy-Item .env.example .env
```

**Edita `.env` y cambia las contraseñas, el correo admin y `JWT_SECRET` (64+ bytes) antes de continuar.** No compartas ese archivo ni lo subas a GitHub. Evita caracteres reservados de URL en las credenciales de Mongo, o codifícalos.

```powershell
docker compose up --build -d
docker compose ps
docker compose logs -f app
```

La aplicación escucha en http://localhost:8081. PostgreSQL en 5433, MongoDB en 27018 y Redis en 6380, enlazados a localhost para no interferir con el proyecto anterior.

El perfil `docker` utiliza `ddl-auto=update` **solo para demostración local**. El perfil `prod` usa `validate` y necesita migraciones SQL versionadas antes de un despliegue real.

## Flujo rápido

1. `POST /api/v1/auth/register` registra un paciente.
2. `POST /api/v1/auth/login` devuelve `accessToken`.
3. Paciente: `GET /api/v1/patients/me` y `PUT /api/v1/patients/me`.
3. Admin: `POST /api/v1/admin/offices` y `POST /api/v1/admin/doctors`.
4. Paciente: `POST /api/v1/appointments`, `GET /api/v1/appointments/me`, `PATCH /api/v1/appointments/{id}/cancel`.
5. Médico: `GET /api/v1/appointments/doctor/me`, `POST /api/v1/appointments/{id}/clinical-notes` (Docker), `POST /api/v1/appointments/{id}/prescriptions`.
6. Paciente: `GET /api/v1/clinical-notes/me` (Docker), `GET /api/v1/prescriptions/me`.

Importa `postman/MediConnect.postman_collection.json` y completa las variables de credenciales y IDs. Nunca pegues JWT reales en un repositorio público.

## Administrador de desarrollo

En DEV, antes de iniciar el servidor, puedes establecer:

```powershell
$env:DEV_ADMIN_EMAIL='admin@local.test'
$env:DEV_ADMIN_PASSWORD='TuClaveSeguraDePrueba'
mvn spring-boot:run
```

En Docker, las mismas variables se leen del archivo `.env`. No existe registro público de administradores.

## Calidad y alcance

```powershell
mvn test
```

GitHub Actions ejecuta pruebas de Spring Boot, seguridad y reglas de negocio. Los tests usan H2; **aún se requiere comprobar manualmente el despliegue integrado Docker/PostgreSQL/MongoDB/Redis y el comportamiento real de OpenFDA**.

Documentación adicional: `docs/requirements-traceability.md`, `docs/plan-de-entrega.md` y `docs/video-script.md`.
