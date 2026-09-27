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
├── dto/
│   ├── ProductoRequest.java           # Entrada de POST/PUT con validaciones (sin id)
│   └── ProductoResponse.java          # Salida de la API (no expone la entidad)
├── mapper/
│   └── ProductoMapper.java            # Conversión DTO ↔ entidad
├── repository/
│   └── ProductoRepository.java        # Acceso a datos (Spring Data JPA)
├── service/
│   ├── ProductoService.java           # Contrato de la lógica de negocio
│   └── ProductoServiceImpl.java       # Implementación del CRUD
├── controller/
│   └── ProductoController.java        # Endpoints REST
└── exception/
    ├── ProductoNoEncontradoException.java
    └── ManejadorGlobalExcepciones.java # Errores en formato ProblemDetail (RFC 9457)

src/main/resources/static/            # Frontend estatico de prueba (HTML/CSS/JS)
├── index.html
├── style.css
└── app.js
```

## Requisitos

- Java 21
- Maven (se incluye el wrapper `mvnw`, no es necesario tener Maven instalado)

## Cómo ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

Si al arrancar aparece `BindException: La dirección ya se está usando` (o `Port 8080 was already in use`), otro
programa ya ocupa el puerto 8080. Se puede correr en otro puerto, por ejemplo el 8081:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run    # API en http://localhost:8081/api/productos
```

En ese caso todas las direcciones (interfaz web, consola H2 y Postman) usan `8081` en lugar de `8080`.

## Interfaz web de prueba

Al abrir `http://localhost:8080` en el navegador se sirve una pequeña
interfaz (HTML/CSS/JS puro, sin frameworks, servida directamente por
Spring Boot desde `src/main/resources/static/`) que consume la API con
`fetch` y permite:

- Ver la lista de productos (GET).
- Crear un producto con el formulario (POST).
- Editar un producto existente con el botón "Editar" (PUT).
- Eliminar un producto con el botón "Eliminar" (DELETE).

Es solo una ayuda visual para entender el funcionamiento del CRUD; el
entregable formal del taller sigue siendo la evidencia en Postman.

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

`POST` responde `201 Created` con la cabecera `Location` del nuevo recurso. Los ids de la ruta deben ser
positivos.

### Cuerpo JSON de ejemplo (POST / PUT)

```json
{
  "nombre": "Teclado mecanico",
  "descripcion": "Switches rojos",
  "precio": 250000,
  "stock": 15
}
```

### Validación y errores

El cuerpo se valida antes de llegar al servicio: `nombre` obligatorio (máx. 100 caracteres), `descripcion`
opcional (máx. 500), `precio` mayor que cero con hasta 2 decimales (`BigDecimal`) y `stock` mayor o igual
que cero. El `id` nunca se lee del cuerpo, así que un `POST` no puede sobrescribir un producto existente.

Todos los errores usan `application/problem+json`:

```json
{
  "title": "Datos inválidos",
  "status": 400,
  "detail": "Los datos del producto no son válidos",
  "instance": "/api/productos",
  "errores": { "precio": "El precio debe ser mayor que cero" }
}
```

## Pruebas

```bash
./mvnw test
```

| Clase | Qué verifica |
|---|---|
| `ProductoServiceImplTest` | Lógica del servicio con el repositorio simulado (Mockito) |
| `ProductoControllerTest` | Rutas, códigos 201/204/400/404, cabecera `Location` y formato de errores (`@WebMvcTest`) |
| `ProductoApiIntegrationTest` | CRUD completo sobre H2 y que un `id` en el cuerpo no sobrescriba otro producto |
| `ProductosApiApplicationTests` | Arranque del contexto de Spring |

## Probando con Postman

1. **GET** `http://localhost:8080/api/productos` → `200 OK` con la lista (vacía al inicio).
2. **POST** `http://localhost:8080/api/productos` con el JSON de ejemplo en el body (raw/JSON) → `201 Created` con el producto creado (incluye `id`).
3. **GET** `http://localhost:8080/api/productos/1` → `200 OK` con el producto creado.
4. **PUT** `http://localhost:8080/api/productos/1` con un JSON modificado en el body → `200 OK` con los datos actualizados.
5. **DELETE** `http://localhost:8080/api/productos/1` → `204 No Content`.
6. **GET** `http://localhost:8080/api/productos/1` nuevamente → `404 Not Found` con un cuerpo `ProblemDetail`.

Toma un pantallazo de cada uno de estos pasos para el informe de entrega.

## Nota de diseño

La guía del taller advertía que exponer directamente la entidad JPA (`@Entity`) mezcla el contrato de la API
con la persistencia. Esta versión separa ambas cosas: los controladores solo reciben y devuelven DTOs
(`ProductoRequest`, `ProductoResponse`) y `ProductoMapper` traduce hacia la entidad. Así se puede cambiar el
esquema de la base sin romper a los clientes de la API, y se evita la asignación masiva de campos como el `id`.
La entidad sigue anotada con JPA, como pide la guía para este taller.

## Integrantes

- Santiago Caicedo

- Adrian Araujo

- Ivan Alexander Lopez
