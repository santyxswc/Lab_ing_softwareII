# Laboratorio de Ingeniería de Software II

Repositorio de prácticas de arquitectura y diseño de software.

## Módulos del Proyecto

### 1. `banco-preguntas-microkernel`
Implementación de un sistema de procesamiento de banco de preguntas utilizando el patrón de arquitectura **Microkernel (Plugin + Pipeline)** en Java.
- **Pipeline de filtros**: Validación de contenido, opciones, respuesta correcta y clasificación.
- **Plugins**: Soporte para preguntas de selección múltiple, casos de estudio y multimedia.

### 2. `spring`
Microservicio RESTful para la gestión (CRUD) de la entidad `Producto` construido con **Spring Boot**, **Spring Data JPA** y base de datos en memoria **H2**.
- Arquitectura en capas: Controlador REST, Servicio, Repositorio y Modelo.
- Incluye interfaz web de prueba y suite de pruebas unitarias.
