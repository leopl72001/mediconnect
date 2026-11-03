# Guion para sustentacion (5 a 7 minutos)

1. Mostrar el repositorio, la estructura package-by-feature y GitHub Actions.
2. Abrir /actuator/health y Swagger. Explicar perfiles dev y docker.
3. Registrar paciente y hacer login JWT. Probar ruta de admin con token paciente: HTTP 403.
4. Como admin, crear consultorio y medico; listar especialidades y doctores.
5. Como paciente, reservar cita futura y repetir horario: HTTP 409.
6. Intentar cancelar cita a 24 h o menos: HTTP 400.
7. En perfil docker: crear nota clinica como medico asignado, consultar como paciente. Mostrar MongoDB.
8. Crear receta y explicar consulta OpenFDA, circuit breaker y fallback MANUAL_VALIDATION_REQUIRED.
9. Mostrar PostgreSQL, Redis, Actuator, virtual threads y documentacion.

**Preparacion:** usar solo datos ficticios, definir credenciales propias y comprobar los contenedores antes de grabar. No mostrar JWT, secretos ni archivos .env en pantalla.
