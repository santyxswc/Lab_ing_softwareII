/**
 * @file CrearPreguntaServiceTest.java
 * @brief Pruebas unitarias para el servicio de aplicación y caso de uso CrearPreguntaUseCase.
 * @author Santiago Caicedo
 */
package co.edu.unicauca.saberpro.application;

import co.edu.unicauca.saberpro.application.service.CrearPreguntaService;
import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaCommand;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Aplicación :: Caso de Uso - CrearPreguntaService")
class CrearPreguntaServiceTest {

    @Mock
    private PreguntaRepositoryPort repositoryPort;

    private CrearPreguntaService service;

    @BeforeEach
    void setUp() {
        service = new CrearPreguntaService(repositoryPort);
    }

    @Test
    @DisplayName("Debe orquestar la creación, pasar a PENDIENTE_REVISION y persistir la pregunta")
    void debeCrearYPersistirPregunta() {
        CrearPreguntaCommand command = new CrearPreguntaCommand(
            "¿Qué capa no debe contener dependencias de frameworks?",
            "La capa de dominio representa las reglas esenciales de negocio.",
            List.of(
                new CrearPreguntaCommand.OpcionItem("Capa de Dominio", true),
                new CrearPreguntaCommand.OpcionItem("Capa de Infraestructura", false),
                new CrearPreguntaCommand.OpcionItem("Capa de Persistencia", false),
                new CrearPreguntaCommand.OpcionItem("Capa Web", false)
            )
        );

        when(repositoryPort.save(any(Pregunta.class))).thenAnswer(invocation -> {
            Pregunta argumento = invocation.getArgument(0);
            return argumento.conId(1L);
        });

        Pregunta resultado = service.crearPregunta(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, resultado.getEstado());
        verify(repositoryPort, times(1)).save(any(Pregunta.class));
    }

    @Test
    @DisplayName("Debe fallar sin invocar el repositorio si la justificación viola la regla de negocio")
    void debeFallarSiReglaDeNegocioSeViola() {
        CrearPreguntaCommand commandInvalido = new CrearPreguntaCommand(
            "Enunciado de prueba",
            "Corta", // Menos de 15 caracteres
            List.of(
                new CrearPreguntaCommand.OpcionItem("Opción A", true),
                new CrearPreguntaCommand.OpcionItem("Opción B", false),
                new CrearPreguntaCommand.OpcionItem("Opción C", false),
                new CrearPreguntaCommand.OpcionItem("Opción D", false)
            )
        );

        assertThrows(ReglaNegocioException.class, () -> service.crearPregunta(commandInvalido));
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Debe consultar por ID a través del puerto de salida")
    void debeConsultarPorId() {
        when(repositoryPort.findById(5L)).thenReturn(Optional.empty());

        Optional<Pregunta> resultado = service.obtenerPorId(5L);

        assertTrue(resultado.isEmpty());
        verify(repositoryPort, times(1)).findById(5L);
    }
}

