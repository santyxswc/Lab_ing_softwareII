#!/usr/bin/env bash
# @file smoke.sh
# @brief Comprueba el flujo integrador sobre los contenedores (Taller 13).
# @author Santiago Caicedo
#
# Uso: ./scripts/smoke.sh [URL]   (por defecto http://localhost:8083)
# Requiere el ecosistema arriba:  docker compose up -d --build
#
# Verifica: 201 al crear, 400 por invariante del dominio, 202 al enviar a
# revisión y que Lifecycle Service deje la pregunta en PENDING_REVIEW
# después de consumir el mensaje de RabbitMQ.

set -euo pipefail
URL="${1:-http://localhost:8083}"
JSON="Content-Type: application/json"

fallar() { echo "FALLO: $1" >&2; exit 1; }

# Cuerpo de la petición; $1 es la justificación.
cuerpo() {
  cat <<JSON_BODY
{"statement":"¿Qué patrón GoF construye objetos complejos paso a paso?",
 "justification":"$1",
 "options":[{"label":"A","text":"Builder"},{"label":"B","text":"Singleton"},
            {"label":"C","text":"Observer"},{"label":"D","text":"Adapter"}],
 "correctAnswer":"A"}
JSON_BODY
}

echo "Esperando a Question Service en $URL ..."
for _ in $(seq 1 60); do
  [ "$(curl -s -o /dev/null -w "%{http_code}" "$URL/questions" || true)" = "200" ] && break
  sleep 2
done
[ "$(curl -s -o /dev/null -w "%{http_code}" "$URL/questions")" = "200" ] || fallar "Question Service no respondió"

resp=$(curl -s -w "\n%{http_code}" -X POST "$URL/questions" -H "$JSON" \
  -d "$(cuerpo "El patrón Builder separa la construcción de un objeto de su representación.")")
code=$(tail -n1 <<<"$resp")
id=$(head -n1 <<<"$resp" | sed -nE "s/^\{\"id\":([0-9]+).*/\1/p")
[ "$code" = "201" ] || fallar "crear pregunta devolvió $code (se esperaba 201)"
echo "  [201] pregunta creada con id=$id en DRAFT"

code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$URL/questions" -H "$JSON" -d "$(cuerpo "corta")")
[ "$code" = "400" ] || fallar "justificación corta devolvió $code (se esperaba 400)"
echo "  [400] justificación corta rechazada por el dominio"

code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$URL/questions/$id/submit")
[ "$code" = "202" ] || fallar "enviar a revisión devolvió $code (se esperaba 202)"
echo "  [202] solicitud de revisión encolada en RabbitMQ"

for _ in $(seq 1 15); do
  estado=$(curl -s "$URL/questions/$id" | sed -nE "s/.*\"status\":\"([A-Z_]+)\".*/\1/p")
  [ "$estado" = "PENDING_REVIEW" ] && break
  sleep 1
done
[ "$estado" = "PENDING_REVIEW" ] || fallar "la pregunta quedó en $estado (se esperaba PENDING_REVIEW)"
echo "  [AMQP] Lifecycle Service procesó el mensaje: DRAFT -> PENDING_REVIEW"

echo "Flujo integrador correcto."
