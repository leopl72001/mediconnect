# Estado de MediConnect — entrega académica

## Entregables

- **Repositorio público:** [leopl72001/mediconnect](https://github.com/leopl72001/mediconnect) — publicado.
- **Video explicativo:** pendiente de grabación. Guion listo en [video-script.md](video-script.md).
- **Informe escrito:** no requerido para esta entrega, según las instrucciones comunicadas por el estudiante.

## Funcionalidades y comprobaciones realizadas

| Función | Evidencia de comprobación |
|---|---|
| Java 21 / Maven / Spring Boot | `mvn clean test`: 14 pruebas exitosas; inicio local correcto |
| Autenticación JWT, BCrypt y roles | Registro/login funcional; paciente rechazado en ruta administrativa con HTTP 403 |
| Perfil de paciente | Consulta y actualización mediante API confirmadas |
| Especialidades, médicos y consultorios | Catálogos y creación administrativa comprobados en DEV y Docker |
| Reservas de citas | Creación `SCHEDULED` y consulta de agenda confirmadas |
| Prevención de solapamientos | HTTP 409 ante reserva duplicada |
| Cancelación de citas | `CANCELLED` cuando corresponde; HTTP 400 con menos de 24 h |
| PostgreSQL | Aplicación ejecutada con perfil Docker y consultas/reservas realizadas |
| MongoDB | Nota clínica creada por médica y visible para el paciente |
| OpenFDA | Receta creada, estado `VALIDATED` en medicamento de prueba; paciente pudo consultarla |
| Redis | `PONG`; caché de médicos con TTL observado de 596 segundos |
| Docker Compose | Aplicación, PostgreSQL, MongoDB y Redis iniciados; `/actuator/health` = `UP` |
| Swagger | Endpoints disponibles en `/swagger-ui.html` |
| GitHub Actions | Compilación/pruebas y smoke test Docker exitosos en el último commit de código revisado |

## Recomendaciones para el video

Seguir [el guion de 5 a 7 minutos](video-script.md). Mostrar GitHub, Swagger, Docker y pruebas representativas. No repetir todas las pruebas en vivo ni mostrar `.env`, contraseñas o JWT.

## Límites del proyecto

- La comprobación `VALIDATED` indica una coincidencia con información de OpenFDA; **no constituye una validación clínica de tratamiento o dosis**.
- La configuración `docker` usa `ddl-auto=update` para demostración local. Para producción se necesitarían migraciones SQL versionadas, gestión robusta de secretos y pruebas adicionales.
- Las comprobaciones manuales y CI no sustituyen pruebas de carga, revisión de seguridad ni auditoría clínica.
- Las pruebas automatizadas con Testcontainers siguen siendo una mejora posible, no parte de los entregables especificados.
