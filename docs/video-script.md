# Guion de sustentación de MediConnect (5 a 7 minutos)

**Objetivo de la entrega:** mostrar el proyecto funcionando y compartir el repositorio público de GitHub. No se requiere un informe escrito.

## Preparación antes de grabar

1. Abrir el repositorio [leopl72001/mediconnect](https://github.com/leopl72001/mediconnect).
2. Encender Docker Desktop y levantar los servicios con `docker compose up -d`. No ejecutar `docker compose down -v`, porque elimina los volúmenes de datos.
3. Verificar `docker compose ps` y `http://localhost:8081/actuator/health` (respuesta `{"status":"UP"}`).
4. Abrir `http://localhost:8081/swagger-ui.html` y tener preparada la pestaña **Actions** de GitHub.
5. Usar solo datos ficticios. **No mostrar `.env`, claves, tokens JWT, contraseñas ni la salida completa del login.** Si se muestra autenticación, ocultar el campo `accessToken` y los encabezados Authorization.

## Guion minuto a minuto

### 0:00–0:45 — Presentación

**Decir:** «Mi proyecto se llama MediConnect. Es un sistema de gestión de citas médicas desarrollado como una API REST. Permite manejar pacientes, médicos, consultorios, reservas, notas clínicas y prescripciones, aplicando reglas de acceso y de negocio».

**Mostrar:** portada del README y estructura del repositorio en GitHub.

### 0:45–1:30 — Tecnologías y arquitectura

**Decir:** «El backend usa Java 21 y Spring Boot. PostgreSQL almacena los datos transaccionales; MongoDB, las notas clínicas; y Redis, la caché. Docker Compose permite ejecutar la aplicación y los tres servicios. La seguridad usa JWT, BCrypt y roles de paciente, médico y administrador».

**Mostrar:** `pom.xml`, paquetes bajo `src/main/java`, `docker-compose.yml` y los cuatro contenedores en Docker Desktop o `docker compose ps`.

### 1:30–2:15 — Servicio disponible y documentación

**Decir:** «La aplicación expone un endpoint de salud y documenta sus operaciones con Swagger/OpenAPI. Las rutas protegidas exigen un token y permisos según el rol».

**Mostrar:** `/actuator/health` con `UP`, Swagger con `auth-controller`, `patient-controller`, `appointment-controller`, `admin-controller`.

### 2:15–3:15 — Pacientes y citas

**Decir:** «Un paciente puede registrarse, iniciar sesión, consultar su perfil y agendar una cita con un médico y un consultorio. Si se intenta reservar un horario solapado con otra cita de ese médico, el sistema devuelve el error HTTP 409. También impide cancelar cuando faltan 24 horas o menos; esa solicitud devuelve HTTP 400».

**Mostrar:** rutas `POST /api/v1/auth/register`, `POST /api/v1/appointments`, `GET /api/v1/appointments/me`. Explicar las respuestas 409 y 400; no es necesario reproducir cada prueba si ya se cuenta con las evidencias.

### 3:15–4:20 — Datos clínicos y OpenFDA

**Decir:** «La médica asignada puede guardar una nota clínica, que se conserva en MongoDB; el paciente puede consultar únicamente sus notas. La médica también puede crear una prescripción. MediConnect consulta OpenFDA para comprobar si encuentra información sobre el medicamento. Cuando la consulta externa falla, la aplicación utiliza un mecanismo de respaldo y solicita validación manual».

**Mostrar:** rutas de notas clínicas y prescripciones en Swagger. Si se usa la respuesta `VALIDATED`, aclarar que significa que se encontró información en OpenFDA, **no que se haya confirmado que la dosis o el tratamiento sean médicamente adecuados**.

### 4:20–5:05 — Redis, Docker y pruebas

**Decir:** «Redis almacena temporalmente resultados para reducir consultas repetidas. La caché del catálogo de médicos tiene una duración de diez minutos. Además, el proyecto cuenta con pruebas automatizadas mediante Maven y GitHub Actions».

**Mostrar:** una consulta a `GET /api/v1/doctors`, `docker compose exec -T redis redis-cli ping` (`PONG`) y `docker compose exec -T redis redis-cli TTL "doctorsBySpecialty::all"` (valor positivo hasta 600). Después mostrar GitHub Actions en verde.

### 5:05–5:45 — Repositorio y conclusión

**Decir:** «El código está publicado en el repositorio leopl72001/mediconnect. La aplicación se probó localmente con PostgreSQL, MongoDB y Redis. En el desarrollo aprendí a integrar bases de datos SQL y NoSQL, aplicar seguridad por roles y prevenir conflictos al reservar citas».

**Mostrar:** página principal del repositorio, README y pestaña Actions. Terminar con la URL del repositorio visible.

## Pruebas realizadas durante la preparación

- `mvn clean test`: **14 pruebas, 0 fallos, BUILD SUCCESS**.
- Spring Boot en modo `dev`: `/actuator/health` respondió `UP`.
- Registrar y autenticar pacientes; consultar y actualizar el perfil.
- Crear consultorio y médica como administrador.
- Reservar cita, rechazar solapamiento (`409`), cancelar con anticipación y bloquear cancelación tardía (`400`).
- Impedir acceso de paciente a ruta administrativa (`403`).
- Consultar agenda médica como rol doctor.
- Docker: PostgreSQL, MongoDB, Redis y aplicación iniciados; endpoint de salud en `UP`.
- Nota clínica guardada en MongoDB y visible para su paciente.
- Receta creada; consulta de prueba a OpenFDA con estado `VALIDATED`; receta visible para paciente.
- Redis: `PONG` y TTL de médicos de **596 segundos** tras consulta.
- CI Java y prueba integrada Docker exitosas en GitHub Actions.

**Alcance:** estos resultados acreditan la demostración funcional de un proyecto académico. No equivalen a una validación médica, una auditoría de seguridad ni una puesta en producción. La grabación y edición del video deben realizarse en el equipo del estudiante.
