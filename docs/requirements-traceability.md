# Trazabilidad tecnica

| Requisito | Implementacion |
|---|---|
| Java 21, Spring Boot, Maven | pom.xml, Mediconnect2Application |
| Seguridad stateless | config/SecurityConfig.java |
| JWT HS512 y BCrypt | config/JwtConfig.java, auth/AuthService.java |
| Roles y @PreAuthorize | auth/UserRole.java, appointments/AppointmentService.java |
| Perfil de paciente | patients/PatientController.java, PatientService.java |
| PostgreSQL / H2 | application-docker.properties / application-dev.properties |
| MongoDB y auditoria | clinical/ClinicalNoteDocument.java, ClinicalNoteService.java |
| Redis TTL | config/CacheConfig.java, catalog/CatalogController.java, prescriptions/OpenFdaService.java |
| Citas atomicas y 409 | appointments/AppointmentService.java, DoctorRepository.java |
| Cancelacion >24 h | appointments/AppointmentPolicy.java |
| OpenFDA, circuit breaker, fallback | prescriptions/OpenFdaService.java |
| Records | DTOs de controladores |
| MapStruct | catalog/SpecialtyMapper.java |
| ProblemDetail | shared/ApiExceptionHandler.java |
| Swagger/OpenAPI | springdoc y /swagger-ui.html |
| Virtual Threads | application.properties |
| Actuator y metricas | observability/Mediconnect2Endpoint.java |
| Docker Compose | Dockerfile, docker-compose.yml |
| Tests / CI | src/test y .github/workflows/ci.yml |
| Postman | postman/MediConnect.postman_collection.json |

**Limites:** El perfil docker usa Hibernate `update` solo para demostracion local. El perfil prod usa `validate` y necesita migraciones SQL versionadas antes de desplegar. Las pruebas de CI usan H2 y no sustituyen pruebas integradas de PostgreSQL/MongoDB/Redis.
