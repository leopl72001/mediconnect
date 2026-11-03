# Estado de la entrega

| Requisito | Estado |
|---|---|
| Java 21 / Spring Boot / Maven | Implementado |
| JWT HS512, BCrypt, roles, @PreAuthorize | Implementado |
| Citas atómicas, solapamientos 409, cancelación >24 h | Implementado |
| DTOs records, MapStruct, ProblemDetail | Implementado |
| Swagger, Actuator, métricas, virtual threads | Implementado |
| PostgreSQL / MongoDB / Redis | Integrados en perfil Docker; requieren prueba de extremo a extremo |
| Notas clínicas con autoría y acceso por paciente | Implementado en perfil Docker |
| OpenFDA + circuit breaker + fallback | Implementado; requiere prueba de proveedor real |
| Redis TTL 10 min / 24 h | Configurado en perfil Docker |
| Docker Compose y Dockerfile | Implementado |
| Tests unitarios, integración H2, CI | Implementados inicialmente |
| Colección Postman | Implementada, variables a configurar |
| Migraciones SQL para producción | Pendiente |
| Testcontainers PostgreSQL/MongoDB/Redis | Pendiente |
| Video de sustentación | Guion incluido; grabación pendiente |

**No confundir la ejecución verde de Maven con una verificación completa del despliegue Docker.**
