# API REST de Productos — Taller 06 (Ingeniería de Software II)

Microservicio que expone un CRUD de la entidad `Producto` usando Spring Boot,
Spring Data JPA y una base de datos H2 en memoria, siguiendo la arquitectura
en capas pedida en la guía del taller: **modelo → repositorio → servicio →
controlador**.

## Estructura del proyecto

```
src/main/java/edu/unicauca/productosapi/
├── ProductosApiApplication.java       # Clase principal (arranque de Spring Boot)
├── model/
│   └── Producto.java                  # Entidad JPA (capa de dominio)
├── repository/
│   └── ProductoRepository.java        # Acceso a datos (Spring Data JPA)
├── service/
│   ├── ProductoService.java           # Contrato de la lógica de negocio
│   └── ProductoServiceImpl.java       # Implementación del CRUD
├── controller/
│   └── ProductoController.java        # Endpoints REST
└── exception/
    ├── ProductoNoEncontradoException.java
    └── ManejadorGlobalExcepciones.java # Traduce excepciones a respuestas HTTP
```

## Requisitos

- Java 21
- Maven (se incluye el wrapper `mvnw`, no es necesario tener Maven instalado)

## Cómo ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

La consola de H2 (para inspeccionar la base de datos en memoria) está en
`http://localhost:8080/h2-console` con:
- JDBC URL: `jdbc:h2:mem:productosdb`
- Usuario: `sa`
- Password: (vacío)

## Endpoints

| Verbo  | Ruta                     | Descripción                     |
|--------|--------------------------|----------------------------------|
| GET    | `/api/productos`         | Lista todos los productos        |
| GET    | `/api/productos/{id}`    | Consulta un producto por id      |
| POST   | `/api/productos`         | Crea un producto                 |
| PUT    | `/api/productos/{id}`    | Actualiza un producto existente  |
| DELETE | `/api/productos/{id}`    | Elimina un producto              |

### Cuerpo JSON de ejemplo (POST / PUT)

```json
{
  "nombre": "Teclado mecanico",
  "descripcion": "Switches rojos",
  "precio": 250000,
  "stock": 15
}
```

## Probando con Postman

1. **GET** `http://localhost:8080/api/productos` → `200 OK` con la lista (vacía al inicio).
2. **POST** `http://localhost:8080/api/productos` con el JSON de ejemplo en el body (raw/JSON) → `201 Created` con el producto creado (incluye `id`).
3. **GET** `http://localhost:8080/api/productos/1` → `200 OK` con el producto creado.
4. **PUT** `http://localhost:8080/api/productos/1` con un JSON modificado en el body → `200 OK` con los datos actualizados.
5. **DELETE** `http://localhost:8080/api/productos/1` → `204 No Content`.
6. **GET** `http://localhost:8080/api/productos/1` nuevamente → `404 Not Found` con un cuerpo JSON descriptivo.

Toma un pantallazo de cada uno de estos pasos para el informe de entrega.

## Nota de diseño

Tal como advierte la guía del taller, en esta primera versión la entidad de
negocio (`Producto`) está anotada directamente con JPA (`@Entity`, `@Id`,
etc.), lo cual mezcla el modelo de negocio con la persistencia. Es válido
para un prototipo rápido, pero no separa responsabilidades; una iteración
posterior del laboratorio abordará una metodología centrada en el negocio
que corrija este acoplamiento.
