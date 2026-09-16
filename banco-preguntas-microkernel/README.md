# Banco de Preguntas Saber Pro — Microkernel + Pipes & Filters

Taller 05 del Laboratorio de Ingeniería de Software II — Universidad del Cauca.
Implementa el patrón **Microkernel (Plug-in)** combinado con **Tuberías y
Filtros (Pipes & Filters)** para la generación extensible de preguntas del
Banco de Preguntas Saber Pro.

## Arquitectura

```
Main (Swing)
   │
   ▼
QuestionMicrokernel  ──(Reflexión, plugins.properties)──► QuestionPlugin (interfaz)
   │  Map<String, Question>                                     ▲
   │                                                             │
   │                                          MultipleChoiceQuestionPlugin
   │                                          CaseQuestionPlugin
   │                                          MultimediaQuestionPlugin
   │                                                             │
   │                                                             ▼
   └────────────────────────────────────────────────  QuestionPipeline
                                                        (ContentValidationFilter →
                                                         OptionsValidationFilter →
                                                         ClassificationFilter →
                                                         CorrectAnswerValidationFilter)
```

- **Microkernel**: `QuestionMicrokernel` no conoce en tiempo de compilación
  ninguna clase de plugin concreta. Lee `plugins.properties` y usa
  `Class.forName(...).getDeclaredConstructor().newInstance()` (Reflexión) para
  instanciar cada plugin — el mismo mecanismo visto en clase con
  `DeliveryPluginManager` para el envío de paquetes a distintos países.
- **Pipes & Filters**: `QuestionPipeline` ejecuta una lista de `QuestionFilter`
  en orden, deteniéndose (fail-fast) en el primero que falle.
  `MultipleChoiceQuestionPlugin` integra los 4 filtros obligatorios.

## Estructura de paquetes

Sigue exactamente el árbol de la guía del taller:

```
co.edu.unicauca.microkernel
├── app          → Main.java (interfaz Swing)
├── common
│   ├── entities    → Question, QuestionRequest
│   └── interfaces  → QuestionPlugin
├── core         → QuestionMicrokernel
├── pipeline
│   ├── base     → QuestionFilter, QuestionPipeline
│   └── filters  → los 4 filtros de validación
└── plugins      → MultipleChoiceQuestionPlugin, CaseQuestionPlugin, MultimediaQuestionPlugin
```

## Plugins implementados

| Plugin | Tipo soportado | Validación |
|---|---|---|
| `MultipleChoiceQuestionPlugin` | `MULTIPLE_CHOICE` | Pipeline completo (4 filtros) |
| `CaseQuestionPlugin` | `CASE_STUDY` | `ContentValidationFilter` + `ClassificationFilter` (reutilizados directamente) |
| `MultimediaQuestionPlugin` | `MULTIMEDIA` | `ContentValidationFilter` |

## Ejecución

```
mvn clean test        # corre las pruebas unitarias
mvn exec:java          # abre la interfaz Swing
```

## Pruebas unitarias

- `QuestionPipelineTest`: cada uno de los 4 filtros probado de forma aislada
  (caso válido e inválido), más el pipeline completo ensamblado.
- `QuestionMicrokernelTest`: carga real de los 3 plugins vía Reflexión,
  generación exitosa para cada tipo de pregunta, rechazo de una solicitud que
  no pasa el pipeline, y excepción al pedir un tipo no soportado.

## Integrantes

- (completar)
