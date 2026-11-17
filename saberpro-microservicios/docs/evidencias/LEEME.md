# Evidencias — Taller 13 (Entrega Final Integradora, Fase II)

Capturas del despliegue contenerizado y de las pruebas end-to-end del Taller 13 (ya ubicadas en esta carpeta).
Deben conservar **exactamente** estos nombres (el informe [`../INFORME_TECNICO.md`](../INFORME_TECNICO.md), sección 3, las referencia con rutas relativas a esta carpeta):

| Archivo requerido | Qué debe mostrar la captura |
|---|---|
| `01-docker-ps.png` | Salida de `docker compose ps` (o `docker ps`) con los **4 contenedores** `Up`: `saberpro-rabbitmq`, `saberpro-validation-service`, `saberpro-question-service`, `saberpro-lifecycle-service`, con sus puertos. |
| `02-pruebas-rest.png` | Pruebas REST contra `question-service`: `200 OK` (consulta), `201 Created` (creación en `DRAFT`), `400 Bad Request` (invariante violada) y `202 Accepted` con `operationId`. |
| `03-rabbitmq-queues.png` | Consola de RabbitMQ (`http://localhost:15672`) con las colas `question.submit.review.queue` y `question.status.changed.queue`. |
| `04-lifecycle-logs.png` | Logs de `lifecycle-service` (`docker compose logs -f lifecycle-service`): consumo del comando y transición `APPLIED: DRAFT -> PENDING_REVIEW`. |

> Las capturas previas del Parcial 2 (numeradas `01-…` a `14-…` con otros nombres) permanecen en esta carpeta como respaldo; no reemplazan a las cuatro anteriores.

## Cómo obtener las capturas

```bash
docker compose up -d --build            # desde la raíz del repositorio
docker compose ps                       # → 01-docker-ps.png
# ejecutar las llamadas REST (Postman/curl) → 02-pruebas-rest.png
# abrir http://localhost:15672            → 03-rabbitmq-queues.png
docker compose logs -f lifecycle-service # → 04-lifecycle-logs.png
```
