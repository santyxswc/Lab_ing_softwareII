#!/usr/bin/env bash
# @file seed.sh
# @brief Puebla Question Service con preguntas de ejemplo.
# @author Santiago Caicedo
#
# Uso: ./scripts/seed.sh [URL]   (por defecto http://localhost:8083)
# Crea 10 preguntas y envía a revisión las 4 primeras, para tener
# ejemplos en DRAFT y en PENDING_REVIEW. Los datos quedan en data/,
# así que basta con correrlo una vez (repetirlo crea duplicados).

set -euo pipefail
URL="${1:-http://localhost:8083}"

# crear "enunciado" "justificación" "A" "B" "C" "D" "correcta"
crear() {
  local body
  body=$(cat <<JSON
{"statement":"$1","justification":"$2",
 "options":[{"label":"A","text":"$3"},{"label":"B","text":"$4"},
            {"label":"C","text":"$5"},{"label":"D","text":"$6"}],
 "correctAnswer":"$7"}
JSON
)
  local resp code
  resp=$(curl -s -w '\n%{http_code}' -X POST "$URL/questions" -H 'Content-Type: application/json' -d "$body")
  code=$(tail -n1 <<<"$resp")
  local id
  id=$(head -n1 <<<"$resp" | sed -nE 's/^\{"id":([0-9]+).*/\1/p')
  printf '  [%s] id=%-3s %s\n' "$code" "${id:--}" "$1"
  IDS+=("$id")
}

IDS=()
echo "Creando preguntas en $URL ..."
crear "¿Cuánto es 7 x 8?" "Siete veces ocho es igual a cincuenta y seis." "54" "56" "58" "64" "B"
crear "¿Cuál es la capital de Colombia?" "Bogotá es la capital de Colombia desde 1819." "Medellín" "Cali" "Bogotá" "Popayán" "C"
crear "¿Cuántos lados tiene un hexágono?" "El prefijo hexa significa seis, por eso tiene seis lados." "5" "6" "7" "8" "B"
crear "¿Qué planeta es conocido como el planeta rojo?" "Marte se ve rojo por el óxido de hierro de su superficie." "Venus" "Júpiter" "Saturno" "Marte" "D"
crear "¿Cuál es la fórmula química del agua?" "El agua tiene dos átomos de hidrógeno y uno de oxígeno." "H2O" "CO2" "O2" "NaCl" "A"
crear "¿Cuál es el sinónimo de 'rápido'?" "Veloz significa que se mueve con gran rapidez." "Lento" "Veloz" "Pesado" "Tranquilo" "B"
crear "¿Qué estructura de datos sigue el principio LIFO?" "En una pila el último elemento en entrar es el primero en salir." "Cola" "Lista" "Pila" "Árbol" "C"
crear "¿Cuánto es el 25% de 200?" "Un cuarto de doscientos es cincuenta, porque 200 / 4 = 50." "25" "40" "50" "75" "C"
crear "¿Qué patrón GoF construye objetos complejos paso a paso?" "El patrón Builder separa la construcción de un objeto de su representación." "Builder" "Singleton" "Observer" "Adapter" "A"
crear "¿En qué año llegó Colón a América?" "Cristóbal Colón llegó a América el 12 de octubre de 1492." "1492" "1500" "1810" "1453" "A"

echo "Enviando a revisión las 4 primeras ..."
for id in "${IDS[@]:0:4}"; do
  op=$(curl -s -X POST "$URL/questions/$id/submit" | sed -nE 's/.*"operationId":"([^"]+)".*/\1/p')
  echo "  id=$id operationId=$op"
done

sleep 2
echo "Estado final:"
curl -s "$URL/questions" | grep -oE '"id":[0-9]+|"status":"[A-Z_]+"' | paste - - | sed 's/"//g'
