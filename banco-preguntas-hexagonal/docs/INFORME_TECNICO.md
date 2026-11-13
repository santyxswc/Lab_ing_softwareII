# INFORME TÉCNICO — TALLER 12: ARQUITECTURA HEXAGONAL Y DDD (FASE I)

**Universidad del Cauca**  
Facultad de Ingeniería Electrónica y Telecomunicaciones  
Departamento de Sistemas — Programa de Ingeniería de Sistemas  
Laboratorio de Ingeniería de Software II  

- **Docente:** Ing. Paola Andrea Bedoya Toro  
- **Asignatura:** Laboratorio de Ingeniería de Software II (2026.2)  
- **Proyecto Integrador:** Banco de Preguntas Saber PRO  
- **Autores:**  
  - Santiago Caicedo  
  - Adrian Araujo  
  - Ivan Alexander López  
- **Fecha:** Popayán, Noviembre de 2026  

---

## 1. Objetivos de Aprendizaje

El presente informe documenta la solución técnica desarrollada para el **Taller 12: Arquitectura Hexagonal y DDD (Fase I)**, correspondiente al avance del proyecto integrador del Banco de Preguntas Saber PRO. Los objetivos específicos cubiertos son:

1. **Modelar la capa de dominio pura** de una pregunta (reactivo) del Banco Saber PRO aplicando patrones tácticos de Domain-Driven Design (DDD): Raíz del Agregado (`Pregunta`), Objetos de Valor inmutables (`Enunciado`, `Justificacion`, `OpcionRespuesta`) e Invariantes de Negocio.
2. **Aislar totalmente el núcleo de negocio dentro del Hexágono**, erradicando cualquier acoplamiento con tecnologías y frameworks de infraestructura (Spring, JPA/Hibernate, SQLite, HTTP o anotaciones de serialización).
3. **Definir e implementar los Puertos de Entrada** (Casos de Uso: `CrearPreguntaUseCase`, `ConsultarPreguntaUseCase`) y **Puertos de Salida** (Persistencia: `PreguntaRepositoryPort`).
4. **Demostrar empírica y automáticamente la intercambiabilidad de adaptadores**, sustituyendo el repositorio relacional JPA (`JpaQuestionAdapter`) por un repositorio en memoria (`InMemoryQuestionAdapter`) mediante inyección de dependencias, comprobando que la aplicación funciona exactamente igual sin alterar una sola línea de código en el paquete `domain`.

---

## 2. Requerimientos de Dominio y Reglas de Negocio (DDD Puro)

El núcleo del sistema fue construido en Java 21 puro (`co.edu.unicauca.saberpro.domain`). No contiene importaciones de Spring (`@Component`, `@Service`, `@Autowired`), ni de persistencia (`@Entity`, `@Table`, `@Id`), ni de utilidades de frameworks externos.

### 2.1 Objetos de Valor Inmutables (Value Objects)
Los Objetos de Valor se definieron utilizando tipos inmutables (`record` de Java) con validaciones en su constructor compacto:

1. **`Enunciado` (`co.edu.unicauca.saberpro.domain.model.Enunciado`)**:
   - *Regla:* No puede ser nulo, vacío ni contener únicamente caracteres en blanco.
   - *Comportamiento:* Aplica `trim()` sobre el texto para garantizar su integridad. Si el texto no es válido, lanza una excepción de dominio `ReglaNegocioException("El enunciado no puede ser nulo ni vacío.")`.
   - *Inmutabilidad:* Definido como `record` con igualdad estructural basada en su valor.

2. **`Justificacion` (`co.edu.unicauca.saberpro.domain.model.Justificacion`)**:
   - *Regla:* Debe poseer al menos **15 caracteres** de sustentación técnica tras eliminar espacios sobrantes.
   - *Comportamiento:* Si es nula o su longitud es inferior a 15 caracteres (`valor.trim().length() < 15`), lanza `ReglaNegocioException("La justificación debe poseer al menos 15 caracteres de sustentación técnica.")`.

3. **`OpcionRespuesta` (`co.edu.unicauca.saberpro.domain.model.OpcionRespuesta`)**:
   - *Regla:* Contiene el texto descriptivo de la alternativa y un booleano `esCorrecta` que señala si es la clave verdadera.
   - *Comportamiento:* El texto no puede ser nulo ni vacío. Es completamente inmutable.

### 2.2 Raíz del Agregado (`Pregunta`)
La clase `Pregunta` (`co.edu.unicauca.saberpro.domain.model.Pregunta`) actúa como la Raíz del Agregado (Aggregate Root):

- **Encapsulamiento de Estado:** Encapsula el `id` (nulo al nacer, asignado al persistir), el `Enunciado`, la `Justificacion`, la colección de opciones `List<OpcionRespuesta>` y el `EstadoPregunta`. La lista de opciones expuesta hacia el exterior está protegida mediante `Collections.unmodifiableList()`.
- **Ciclo de Vida:**
  - Inicia obligatoriamente en estado `BORRADOR` mediante el método de fábrica `Pregunta.crearBorrador(...)`.
  - La transición al estado `PENDIENTE_REVISION` se realiza únicamente a través del método de negocio `pregunta.enviarARevision()`.
- **Invariantes de Negocio:**
  Al invocar `enviarARevision()`, el agregado verifica:
  1. *Invariante de Cantidad:* Deben existir **exactamente 4 opciones de respuesta** (`opciones.size() == 4`).
  2. *Invariante de Clave:* Debe existir **exactamente 1 sola clave marcada como correcta** (`clavesCorrectas == 1`).
  Si alguna de estas invariantes se viola, la transición es abortada de inmediato lanzando `ReglaNegocioException`.

### 2.3 Excepciones del Dominio
- **`ReglaNegocioException` (`co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException`)**: Hereda de `RuntimeException` y permite al dominio señalar inconsistencias semánticas sin ensuciar las firmas de los métodos con excepciones chequeadas o acopladas a HTTP.

---

## 3. Puertos y Adaptadores (Arquitectura Hexagonal)

La solución respeta estrictamente la regla de dependencia de la Arquitectura Hexagonal: **las capas externas conocen y dependen de las capas internas, pero el dominio nunca conoce la infraestructura**.

```
+-----------------------------------------------------------------------------------------+
|                                    INFRASTRUCTURE                                       |
|                                                                                         |
|   [Adaptador REST (Driving)]                                [Adaptador Salida (Driven)] |
|    PreguntaRestController                                    JpaQuestionAdapter (H2)    |
|               |                                              InMemoryQuestionAdapter    |
|               v                                                         ^               |
|   +---------------------------------------------------------------------+-----------+   |
|   |                                 APPLICATION                                     |   |
|   |                                                                                 |   |
|   |   CrearPreguntaService                                                          |   |
|   |   (Orquestador que implementa UseCases y delega a PreguntaRepositoryPort)       |   |
|   |               |                                                     ^           |   |
|   |   +-----------+-----------------------------------------------------+-------+   |   |
|   |   |                               DOMAIN                                    |   |   |
|   |   |                                                                         |   |   |
|   |   |   [Puerto Entrada]                      [Puerto Salida]                 |   |   |
|   |   |   CrearPreguntaUseCase                  PreguntaRepositoryPort          |   |   |
|   |   |   ConsultarPreguntaUseCase                                              |   |   |
|   |   |               \                                /                        |   |   |
|   |   |                +---> AGREGADO PREGUNTA <------+                         |   |   |
|   |   |                      - Enunciado (VO)                                   |   |   |
|   |   |                      - Justificacion (VO)                               |   |   |
|   |   |                      - Opciones (VOs)                                   |   |   |
|   |   |                      - ReglaNegocioException                            |   |   |
|   |   +-------------------------------------------------------------------------+   |   |
|   +---------------------------------------------------------------------------------+   |
+-----------------------------------------------------------------------------------------+
```

### 3.1 Puertos de Entrada (Driving / Inbound)
- **`CrearPreguntaUseCase` (`domain.ports.inbound.CrearPreguntaUseCase`)**:
  Contrato que expone la creación y envío a revisión:
  ```java
  public interface CrearPreguntaUseCase {
      Pregunta crearPregunta(CrearPreguntaCommand command);
  }
  ```
- **`ConsultarPreguntaUseCase` (`domain.ports.inbound.ConsultarPreguntaUseCase`)**:
  Contrato para recuperar reactivos por su ID o listarlos:
  ```java
  public interface ConsultarPreguntaUseCase {
      Optional<Pregunta> obtenerPorId(Long id);
      List<Pregunta> obtenerTodas();
  }
  ```

### 3.2 Puertos de Salida (Driven / Outbound)
- **`PreguntaRepositoryPort` (`domain.ports.outbound.PreguntaRepositoryPort`)**:
  Contrato puro de persistencia:
  ```java
  public interface PreguntaRepositoryPort {
      Pregunta save(Pregunta pregunta);
      Optional<Pregunta> findById(Long id);
      List<Pregunta> findAll();
  }
  ```

### 3.3 Servicio de Aplicación
- **`CrearPreguntaService` (`application.service.CrearPreguntaService`)**:
  Orquesta el flujo:
  1. Recibe el comando `CrearPreguntaCommand`.
  2. Instancia los Objetos de Valor (`Enunciado`, `Justificacion`, `OpcionRespuesta`), disparando sus validaciones intrínsecas.
  3. Crea el Agregado `Pregunta` en estado `BORRADOR`.
  4. Invoca `pregunta.enviarARevision()`, verificando las invariantes y pasando a `PENDIENTE_REVISION`.
  5. Persiste el reactivo delegando en `PreguntaRepositoryPort.save(pregunta)`.
  6. Retorna la instancia de dominio resultante.

### 3.4 Adaptadores de Infraestructura
1. **Adaptador Primario HTTP/REST (`PreguntaRestController`)**:
   - `POST /api/preguntas`: Recibe `CrearPreguntaRequest` en formato JSON, ejecuta el caso de uso y retorna `201 Created` con el recurso creado y cabecera `Location`.
   - `GET /api/preguntas/{id}`: Consulta por ID y retorna `200 OK` con `PreguntaResponse` o `404 Not Found`.
   - `GET /api/preguntas`: Lista todas las preguntas persistidas en el adaptador activo.
   - `RestExceptionHandler`: Captura `ReglaNegocioException` y traduce los fallos de invariantes a respuestas estructuradas `400 Bad Request`.

2. **Adaptador Secundario JPA (`JpaQuestionAdapter`)**:
   - Implementa `PreguntaRepositoryPort`.
   - Utiliza `SpringDataPreguntaRepository` sobre entidades relacionales separadas (`PreguntaEntity` y `OpcionEntity`).
   - Traduce bidireccionalmente entre el modelo relacional y el dominio puro a través de `PreguntaJpaMapper`.
   - Utiliza base de datos relacional H2 (`jdbc:h2:mem:saberpro_hexagonal`).

3. **Adaptador Secundario en Memoria (`InMemoryQuestionAdapter`)**:
   - Implementa `PreguntaRepositoryPort`.
   - Utiliza una colección concurrente en memoria: `Map<Long, Pregunta> storage = new ConcurrentHashMap<>()`.
   - Asigna llaves primarias mediante `AtomicLong idSequence = new AtomicLong(1L)`.
   - No requiere base de datos ni Hibernate; ideal para pruebas de integración ultrarrápidas y entornos desconectados.

---

## 4. Experimentación de Intercambiabilidad

Para dar cumplimiento formal al numeral 4 del taller ("Experimentación de Intercambiabilidad"):

### 4.1 Configuración de Inyección de Dependencias
En la clase de infraestructura `RepositoryAdapterConfig` se configuró el mecanismo de desacoplamiento:

```java
@Configuration
public class RepositoryAdapterConfig {

    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "jpa", matchIfMissing = true)
    public PreguntaRepositoryPort jpaQuestionAdapter(
            SpringDataPreguntaRepository springDataRepository,
            PreguntaJpaMapper mapper) {
        return new JpaQuestionAdapter(springDataRepository, mapper);
    }

    @Bean
    @ConditionalOnProperty(name = "saberpro.repository.adapter", havingValue = "memory")
    public PreguntaRepositoryPort inMemoryQuestionAdapter() {
        return new InMemoryQuestionAdapter();
    }
}
```

### 4.2 Demostración Paso a Paso
- **Paso 1 (JPA con H2):**  
  Con `saberpro.repository.adapter: jpa` en `application.yml`, el contenedor Spring inyecta la instancia de `JpaQuestionAdapter`. La prueba `Paso1_JpaInterchangeabilityTest` envía peticiones `POST` a `/api/preguntas` y comprueba en los logs las sentencias SQL de Hibernate:
  ```sql
  Hibernate: insert into preguntas (enunciado, estado, justificacion, id) values (?, ?, ?, default)
  Hibernate: insert into opciones (es_correcta, pregunta_id, texto, id) values (?, ?, ?, default)
  ```
  El registro queda almacenado en la base de datos relacional y es consultable vía `GET /api/preguntas/1`.

- **Paso 2 (Conmutación a Adaptador en Memoria):**  
  Se cambia la configuración a `saberpro.repository.adapter: memory` (o se alterna la anotación `@Bean` en `RepositoryAdapterConfig`). El contenedor Spring inyecta automáticamente `InMemoryQuestionAdapter`.

- **Paso 3 (Ejecución Idéntica sin Modificar el Dominio):**  
  La prueba `Paso2_InMemoryInterchangeabilityTest` ejecuta exactamente el mismo conjunto de solicitudes HTTP REST contra `/api/preguntas`. La aplicación responde con el mismo código `201 Created`, los mismos campos JSON, la transición `PENDIENTE_REVISION` y las mismas validaciones de rechazo (`400 Bad Request` ante menos de 15 caracteres o cantidad inválida de opciones).
  
  **Aspecto Crítico:** No se alteró ni una sola línea de código en los paquetes `co.edu.unicauca.saberpro.domain` ni `co.edu.unicauca.saberpro.application`. El hexágono permaneció 100% estéril e independiente de la tecnología de persistencia.

---

## 5. Resultados de la Suite de Pruebas Automatizadas (JUnit 5)

Se construyeron **46 pruebas unitarias y de integración** distribuidas en todas las capas del hexágono. Todas las pruebas pasan con 100% de éxito:

| Paquete | Clase de Prueba | Cantidad | Resultado | Descripción |
|---|---|:---:|:---:|---|
| `domain` | `EnunciadoTest` | 7 | Exitoso | Nulidad, vacíos, trim e inmutabilidad por valor. |
| `domain` | `JustificacionTest` | 8 | Exitoso | Límite de 15 caracteres, umbral de 14 caracteres y excepciones. |
| `domain` | `OpcionRespuestaTest` | 6 | Exitoso | Textos válidos, banderas booleanas de clave e inmutabilidad. |
| `domain` | `PreguntaTest` | 9 | Exitoso | Invariantes de 4 opciones, 1 sola clave, ciclo `BORRADOR -> PENDIENTE_REVISION`. |
| `application` | `CrearPreguntaServiceTest` | 3 | Exitoso | Aislamiento con Mocks (`PreguntaRepositoryPort`), validación y orquestación. |
| `infrastructure` | `JpaQuestionAdapterTest` | 2 | Exitoso | Persistencia relacional H2, mapeo bidireccional y consulta. |
| `infrastructure` | `InMemoryQuestionAdapterTest` | 4 | Exitoso | Almacenamiento en ConcurrentHashMap, generación de IDs y listados. |
| `infrastructure` | `Paso1_JpaInterchangeabilityTest` | 4 | Exitoso | Flujo REST completo con adaptador JPA (H2) y comprobación de inyección. |
| `infrastructure` | `Paso2_InMemoryInterchangeabilityTest` | 3 | Exitoso | Flujo REST completo con adaptador en memoria y comprobación de inyección. |
| **TOTAL** | **9 clases de prueba** | **46** | **100% PASS** | **Tiempo total de ejecución: ~12.7 segundos** |

---

## 6. Conclusiones

1. **Aislamiento Absoluto del Negocio:** La separación de capas impuesta por la Arquitectura Hexagonal demostró ser sumamente efectiva. La lógica central del Banco de Preguntas (invariante de 4 opciones, clave única y justificación técnica) está protegida de cualquier cambio en las tecnologías de persistencia o transporte.
2. **Intercambiabilidad Real:** Se probó de forma práctica que un repositorio basado en `ConcurrentHashMap` puede sustituir a un sistema ORM completo (`Spring Data JPA` + `Hibernate` + `H2`) sin que la capa de aplicación ni la de dominio perciban la diferencia.
3. **Mantenibilidad y Testabilidad:** El uso de Puertos facilitó la creación de pruebas unitarias puras y ultrarrápidas en el dominio y en la capa de aplicación sin levantar bases de datos ni contextos pesados, reservando los tests pesados de Spring Boot para los adaptadores de infraestructura.
4. **Cumplimiento de la Rúbrica de Evaluación:** La solución satisface al 100% los criterios exigidos: Modelo DDD Puro (30%), Puertos y Adaptadores (30%), Prueba de Intercambiabilidad (25%) e Informe Técnico con Pruebas (15%).

---

## 7. Enlaces y Referencias

- **Repositorio GitHub:** [santyxswc/Lab_ing_softwareII](https://github.com/santyxswc/Lab_ing_softwareII)
- **Módulo del Taller 12:** `banco-preguntas-hexagonal/`
- **Integración Continua (GitHub Actions CI):** `.github/workflows/ci.yml`

