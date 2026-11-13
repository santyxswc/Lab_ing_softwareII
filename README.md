# Laboratorio de Ingeniería de Software II

[![CI](https://github.com/santyxswc/Lab_ing_softwareII/actions/workflows/ci.yml/badge.svg)](https://github.com/santyxswc/Lab_ing_softwareII/actions/workflows/ci.yml)

Talleres de arquitectura y diseño de software — Universidad del Cauca.

| Taller | Carpeta | Tema |
|---|---|---|
| 05 | [`banco-preguntas-microkernel`](banco-preguntas-microkernel/README.md) | Patrón **Microkernel (plugins)** combinado con **Tuberías y Filtros**, con interfaz Swing |
| 06 | [`productos-api`](productos-api/README.md) | **API REST** con Spring Boot, Spring Data JPA y H2 (CRUD de productos) |
| Parcial 2 | [`saberpro-microservicios`](saberpro-microservicios/README.md) | **Microservicios** con REST síncrono, **RabbitMQ**, patrones **Builder** y **State**, y JPA limpia |
| 12 | [`banco-preguntas-hexagonal`](banco-preguntas-hexagonal/README.md) | **Arquitectura Hexagonal (Puertos y Adaptadores)** y **DDD** con persistencia intercambiable (JPA / memoria) |

Cada carpeta tiene su propio README con la arquitectura, la estructura de paquetes y cómo probarla.

---

## Taller 05 — Banco de preguntas (Microkernel + Pipes & Filters)

Generador de preguntas del banco Saber Pro. El núcleo (`QuestionMicrokernel`) carga los plugins de cada tipo de
pregunta por reflexión a partir de `plugins.properties`, sin conocer sus clases concretas. Las preguntas de
selección múltiple pasan por un pipeline de cuatro filtros de validación que se detiene en el primero que falla.

- **Plugins:** selección múltiple, análisis de caso y multimedia.
- **Filtros:** contenido, opciones, área de conocimiento y respuesta correcta.

```bash
cd banco-preguntas-microkernel
mvn clean test      # 24 pruebas unitarias
mvn exec:java       # abre la interfaz Swing
```

## Taller 06 — API REST de productos (Spring Boot)

Microservicio con el CRUD de `Producto` en capas: modelo → repositorio → servicio → controlador, con DTOs validados y errores en formato `ProblemDetail` (RFC 9457). Usa una base
de datos H2 en memoria, así que no requiere instalar nada más.

| Verbo | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Consulta un producto (404 si no existe) |
| POST | `/api/productos` | Crea un producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

```bash
cd productos-api
./mvnw spring-boot:run    # API en http://localhost:8080/api/productos
./mvnw test               # 12 pruebas: servicio, capa web e integración
```

Si al arrancar aparece `BindException: La dirección ya se está usando` (o `Port 8080 was already in use`), otro
programa ya ocupa el puerto 8080. Se puede correr en otro puerto, por ejemplo el 8081:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run    # API en http://localhost:8081/api/productos
```

En ese caso todas las direcciones (interfaz web, consola H2 y Postman) usan `8081` en lugar de `8080`.

En `http://localhost:8080` hay una interfaz web para probar el CRUD desde el navegador, y en
`http://localhost:8080/h2-console` la consola de la base de datos.

## Parcial 2 — Microservicios Saber PRO (REST + RabbitMQ)

Tres microservicios Spring Boot para crear reactivos y enviarlos a revisión:

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| `validation-service` | 8082 | Valida que la justificación tenga más de 15 caracteres |
| `question-service` | 8083 | Construye la pregunta con **Builder**, la valida por REST (RestTemplate), la guarda en H2 y publica el comando en RabbitMQ |
| `lifecycle-service` | 8084 | Consume el comando con `@RabbitListener` y aplica la transición `DRAFT → PENDING_REVIEW` con el patrón **State** |

El dominio es Java puro; la persistencia usa entidades `@Entity` separadas (`QuestionRecord`) y un mapeador bidireccional.
Requiere RabbitMQ en `localhost:5672`.

```bash
cd saberpro-microservicios
mvn clean install                               # 30 pruebas
java -jar validation-service/target/validation-service-1.0.0.jar
java -jar question-service/target/question-service-1.0.0.jar
java -jar lifecycle-service/target/lifecycle-service-1.0.0.jar
./scripts/seed.sh                               # preguntas de ejemplo
```

## Taller 12 — Banco de preguntas (Arquitectura Hexagonal + DDD)

Fase I del proyecto integrador: creación y evaluación de preguntas del Banco Saber PRO con **DDD táctico** y
**Arquitectura Hexagonal (Puertos y Adaptadores)**. El paquete `domain` es Java puro (sin Spring, JPA ni HTTP), y la
persistencia se cambia entre JPA y memoria con una sola propiedad, sin tocar el dominio.

- **Objetos de Valor:** `Enunciado` (no vacío), `Justificacion` (mínimo 15 caracteres) y `OpcionRespuesta`.
- **Agregado:** `Pregunta` nace en `BORRADOR` y pasa a `PENDIENTE_REVISION` solo con exactamente 4 opciones y 1 clave correcta.
- **Puertos:** de entrada `CrearPreguntaUseCase` y `ConsultarPreguntaUseCase`; de salida `PreguntaRepositoryPort`.
- **Adaptadores:** REST (`PreguntaRestController`) y persistencia intercambiable: `JpaQuestionAdapter` (H2) e
  `InMemoryQuestionAdapter` (`ConcurrentHashMap`), elegidos con `saberpro.repository.adapter=jpa|memory`.

| Verbo | Ruta | Descripción |
|---|---|---|
| POST | `/api/preguntas` | Crea la pregunta y la envía a revisión (201 / 400) |
| GET | `/api/preguntas` | Lista las preguntas |
| GET | `/api/preguntas/{id}` | Consulta una pregunta (404 si no existe) |

```bash
cd banco-preguntas-hexagonal
./mvnw clean test          # 46 pruebas: dominio, aplicación, adaptadores e intercambiabilidad
./mvnw spring-boot:run     # API en http://localhost:8085/api/preguntas
```

El informe técnico está en [`banco-preguntas-hexagonal/docs/INFORME_TECNICO.md`](banco-preguntas-hexagonal/docs/INFORME_TECNICO.md).

---

## Requisitos

- Java 21
- Maven 3.9 o superior (`productos-api` y `banco-preguntas-hexagonal` incluyen el wrapper `mvnw`)
- RabbitMQ (solo para el Parcial 2)

## Integrantes

- Santiago Caicedo
- Adrian Araujo
- Ivan Alexander Lopez
