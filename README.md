# Coworking Reservation API

## Descripción

API REST desarrollada con **Spring Boot 3** y **Java 17** para la
administración de espacios de coworking, reservas y reportes de
ocupación.

------------------------------------------------------------------------

# Tecnologías utilizadas

-   Java 17
-   Spring Boot 3.2.5
-   Spring Security + JWT
-   Spring Data JPA
-   PostgreSQL
-   MapStruct
-   Lombok
-   Bean Validation (Jakarta Validation)
-   Spring Cache
-   Spring Async
-   Spring Boot Actuator
-   Springdoc OpenAPI (Swagger)
-   Resilience4j Circuit Breaker
-   Mockito
-   JUnit 5
-   Testcontainers
-   JaCoCo
-   Docker
-   Docker Compose

------------------------------------------------------------------------

# Arquitectura

El proyecto sigue una arquitectura por capas:

    Controller
        ↓
    Service
        ↓
    Repository
        ↓
    PostgreSQL

Paquetes principales:

    controller
    service
    repository
    entity
    dto
    mapper
    config
    security
    common
    exception
    pricing

------------------------------------------------------------------------

# Funcionalidades implementadas

-   Autenticación mediante JWT
-   Autorización basada en roles (ADMIN / USER)
-   Administración de espacios
-   Creación y cancelación de reservas
-   Validación de disponibilidad evitando solapamientos
-   Validación de pago mediante servicio externo simulado
-   Reporte de ocupación por rango de fechas
-   Caché de reportes
-   Notificación asíncrona
-   Documentación Swagger
-   Monitoreo mediante Actuator

------------------------------------------------------------------------

# Decisiones de diseño

## Spring Boot 3 + Java 17

Se utilizó Spring Boot 3 junto con Java 17 por ser la versión LTS
recomendada y por ofrecer soporte completo para Jakarta EE, mejor
rendimiento y compatibilidad con las versiones actuales del ecosistema
Spring.

## Spring Data JPA

Se utilizó Spring Data JPA para reducir código repetitivo y facilitar el
acceso a datos.

Las relaciones fueron modeladas utilizando asociaciones JPA y consultas
personalizadas mediante `@Query` cuando fue necesario, evitando
consultas innecesarias y manteniendo un modelo de dominio claro.

## Spring Security + JWT

La autenticación se implementó mediante JWT para mantener una API
completamente stateless.

La autorización utiliza roles (ADMIN y USER) junto con `@PreAuthorize` y
validaciones de propiedad de la reserva para impedir que un usuario
consulte o modifique reservas pertenecientes a otros usuarios.

## Bean Validation

Las validaciones se implementaron utilizando Jakarta Validation sobre
los DTOs de entrada, manteniendo la lógica de validación desacoplada del
código de negocio.

## Manejo centralizado de errores

Se implementó un `@RestControllerAdvice` para centralizar el manejo de
excepciones y devolver respuestas consistentes para:

-   Errores de negocio
-   Validaciones
-   Accesos denegados
-   Errores internos

## Spring Boot Actuator

Se expusieron los siguientes endpoints:

-   `/actuator/health`
-   `/actuator/info`
-   `/actuator/metrics`
-   `/actuator/circuitbreakers`

permitiendo monitorear el estado general de la aplicación.

## Configuración por perfiles

Se utilizaron:

-   application-dev.yml
-   application-prod.yml

La configuración sensible (base de datos) se externalizó mediante
variables de entorno, evitando valores fijos dentro del código.

## Caché

El endpoint del reporte de ocupación utiliza `@Cacheable`, evitando
recalcular la información para solicitudes repetidas sobre el mismo
rango de fechas.

## Procesamiento asíncrono

La notificación posterior a la creación de una reserva se realiza
mediante `@Async`, simulando el envío de un correo sin bloquear la
respuesta HTTP.

## Manejo transaccional

La creación de reservas utiliza `@Transactional`, garantizando
consistencia durante la validación de disponibilidad y el almacenamiento
de la reserva.

## Swagger

La documentación OpenAPI se genera automáticamente mediante Springdoc.

## Testing

El proyecto incluye:

-   Pruebas unitarias con Mockito
-   Pruebas de integración con SpringBootTest
-   Pruebas de controladores
-   Pruebas de seguridad

La cobertura se genera utilizando JaCoCo.

## Docker

Se incluye:

-   Dockerfile
-   docker-compose.yml

permitiendo levantar toda la solución mediante un único comando.

## Circuit Breaker

La llamada al servicio externo de validación de pago está protegida
mediante Resilience4j.

Se configuraron:

-   Failure Rate Threshold
-   Sliding Window
-   Wait Duration
-   Half Open State

Cuando el servicio externo falla, se ejecuta un método *fallback*
devolviendo una respuesta controlada.

El estado del circuito puede consultarse desde:

    /actuator/circuitbreakers

------------------------------------------------------------------------

# Patrón de diseño utilizado

## Strategy

Se implementó el patrón **Strategy** para el cálculo de tarifas.

Cada política de precios se encuentra encapsulada en una estrategia
independiente, permitiendo incorporar nuevas reglas sin modificar el
código existente.

Esto evita grandes bloques de `if/else` y cumple con el principio
**Open/Closed**.

------------------------------------------------------------------------

# Trade-offs

Debido al tiempo disponible para el challenge se priorizó la
implementación de la funcionalidad de negocio y una arquitectura
mantenible.

Para simplificar la evaluación mediante Docker Compose, el perfil de
producción utiliza la generación automática del esquema de base de datos
con Hibernate. En un entorno real se utilizarían migraciones mediante
Flyway o Liquibase.

------------------------------------------------------------------------

# Fuera de alcance

Con más tiempo se incorporarían:

-   Flyway o Liquibase
-   Redis
-   Envío real de correos SMTP
-   Refresh Tokens
-   Paginación
-   CI/CD
-   Prometheus + Grafana
-   Rate Limiting
-   Versionado de API

------------------------------------------------------------------------

# Ejecución del proyecto

## Opción 1 - Maven

Levantar PostgreSQL:

``` bash
docker compose up -d postgres
```

Ejecutar la aplicación:

``` bash
mvn spring-boot:run
```

------------------------------------------------------------------------

## Opción 2 - Docker Compose (Recomendada)

Construir y ejecutar toda la solución:

``` bash
docker compose up --build
```

Se levantarán automáticamente:

-   PostgreSQL
-   API Spring Boot
-   pgAdmin

Detener los servicios:

``` bash
docker compose down
```

Eliminar también el volumen de la base de datos:

``` bash
docker compose down -v
```

------------------------------------------------------------------------

# Servicios disponibles

  Servicio          URL
  ----------------- ------------------------------------------------
  API               http://localhost:8080
  Swagger           http://localhost:8080/swagger-ui/index.html
  Actuator          http://localhost:8080/actuator
  Health            http://localhost:8080/actuator/health
  Circuit Breaker   http://localhost:8080/actuator/circuitbreakers
  pgAdmin           http://localhost:5050

------------------------------------------------------------------------

# Base de datos

Docker Compose crea automáticamente:

-   Base de datos: `coworking_db`
-   Usuario: `coworking_user`
-   Contraseña: `coworking_password`

------------------------------------------------------------------------

# Archivos incluidos

-   README.md
-   Dockerfile
-   docker-compose.yml
-   requests.http
-   Colección de Postman

------------------------------------------------------------------------

# Compilación

Compilar:

``` bash
mvn clean compile
```

Ejecutar pruebas:

``` bash
mvn clean test
```

Generar el JAR:

``` bash
mvn clean package -DskipTests
```

Generar reporte JaCoCo:

``` bash
mvn clean verify
```

El reporte se encuentra en:

    target/site/jacoco/index.html

------------------------------------------------------------------------

# Estado del proyecto

✅ Autenticación JWT

✅ Autorización por roles

✅ Gestión de espacios

✅ Gestión de reservas

✅ Validación de disponibilidad

✅ Validación de pago externa

✅ Circuit Breaker

✅ Notificación asíncrona

✅ Reporte de ocupación con caché

✅ Swagger

✅ Actuator

✅ Docker

✅ Docker Compose

✅ Pruebas unitarias e integración

✅ JaCoCo
