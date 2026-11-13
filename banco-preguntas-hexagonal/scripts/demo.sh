#!/usr/bin/env bash
# Recorre la API del Taller 12: 2 preguntas válidas (201), 3 rechazos por reglas de negocio (400),
# consulta por id (200 / 404) y listado. Requiere la aplicación en http://localhost:8085 y jq.
# Se usó para las evidencias 01 y 03: ejecutarlo con adapter=jpa y con adapter=memory da la misma salida.
B=http://localhost:8085/api/preguntas
J='Content-Type: application/json'
op(){ printf '{"texto":"%s","esCorrecta":%s}' "$1" "$2"; }
body(){ # enunciado justificacion opciones...
  local e="$1" j="$2"; shift 2; local o; o=$(IFS=,; echo "$*")
  printf '{"enunciado":"%s","justificacion":"%s","opciones":[%s]}' "$e" "$j" "$o"; }
call(){ local title="$1" method="$2" url="$3" data="$4"
  echo; echo "▶ $title"
  if [ -n "$data" ]; then echo "\$ curl -X $method $url  (JSON)"; r=$(curl -s -w '\n%{http_code}' -X $method -H "$J" -d "$data" $url)
  else echo "\$ curl -X $method $url"; r=$(curl -s -w '\n%{http_code}' $url); fi
  code=$(tail -n1 <<<"$r"); json=$(sed '$d' <<<"$r")
  echo "HTTP $code"; [ -n "$json" ] && jq -c "${5:-.}" <<<"$json"; }
V4=("$(op 'Definir contratos del núcleo' true)" "$(op 'Conectarse al driver JDBC' false)" "$(op 'Configurar anotaciones Spring' false)" "$(op 'Serializar a JSON' false)")
call "1. Crear pregunta válida (BORRADOR → PENDIENTE_REVISION)" POST $B "$(body '¿Qué definen los puertos en Arquitectura Hexagonal?' 'Los puertos son contratos independientes de la tecnología.' "${V4[@]}")" '{id,estado,enunciado,opciones:(.opciones|length)}'
call "2. Crear segunda pregunta válida" POST $B "$(body '¿Qué es un Objeto de Valor en DDD?' 'Es inmutable y se compara por sus atributos, no por identidad.' "$(op 'Objeto inmutable comparado por valor' true)" "$(op 'Entidad con identidad propia' false)" "$(op 'Servicio de infraestructura' false)" "$(op 'Repositorio JPA' false)")" '{id,estado,enunciado,opciones:(.opciones|length)}'
call "3. Justificación con menos de 15 caracteres → rechazada" POST $B "$(body '¿Qué es un agregado?' 'Muy corta' "${V4[@]}")" '{status,error,mensaje}'
call "4. Dos claves correctas → invariante del Agregado" POST $B "$(body '¿Qué es un adaptador?' 'Implementa un puerto con una tecnología concreta.' "$(op 'A' true)" "$(op 'B' true)" "$(op 'C' false)" "$(op 'D' false)")" '{status,error,mensaje}'
call "5. Solo 3 opciones → invariante de cantidad" POST $B "$(body '¿Qué es un puerto?' 'Es una interfaz definida por el núcleo del hexágono.' "$(op 'A' true)" "$(op 'B' false)" "$(op 'C' false)")" '{status,error,mensaje}'
call "6. Consultar la pregunta 1" GET $B/1 "" '{id,estado,enunciado,justificacion}'
call "7. Consultar una pregunta inexistente" GET $B/99 ""
call "8. Listar preguntas (solo se guardaron las válidas)" GET $B "" 'map({id,estado})'
