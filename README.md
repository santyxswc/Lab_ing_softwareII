# Laboratorio de Ingeniería de Software II

Talleres de arquitectura y diseño de software — Universidad del Cauca.

| Taller | Carpeta | Tema |
|---|---|---|
| 05 | [`banco-preguntas-microkernel`](banco-preguntas-microkernel/README.md) | Patrón **Microkernel (plugins)** combinado con **Tuberías y Filtros**, con interfaz Swing |
| 06 | [`APIREST_con_spirngBoot`](APIREST_con_spirngBoot/README.md) | **API REST** con Spring Boot, Spring Data JPA y H2 (CRUD de productos) |

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
mvn clean test      # 19 pruebas unitarias
mvn exec:java       # abre la interfaz Swing
```

## Taller 06 — API REST de productos (Spring Boot)

Microservicio con el CRUD de `Producto` en capas: modelo → repositorio → servicio → controlador. Usa una base
de datos H2 en memoria, así que no requiere instalar nada más.

| Verbo | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Consulta un producto (404 si no existe) |
| POST | `/api/productos` | Crea un producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

```bash
cd APIREST_con_spirngBoot
./mvnw spring-boot:run    # API en http://localhost:8080/api/productos
./mvnw test               # prueba de arranque del contexto
```

En `http://localhost:8080` hay una interfaz web para probar el CRUD desde el navegador, y en
`http://localhost:8080/h2-console` la consola de la base de datos.

---

## Requisitos

- Java 21
- Maven 3.9 o superior (la API incluye el wrapper `mvnw`)

## Documentación del código

Todo el código está documentado con comentarios Doxygen/Javadoc (`@file`, `@brief`, `@param`, `@return`,
`@throws`). Para generar la documentación HTML de ambos talleres desde la raíz del repositorio:

```bash
doxygen            # usa el Doxyfile de la raíz; resultado en docs/html/index.html
```

---

## Integrantes

- Santiago Caicedo
- Adrian Araujo
- Ivan Alexander Lopez
