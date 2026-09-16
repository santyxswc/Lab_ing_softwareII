package co.edu.unicauca.microkernel.app;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.core.QuestionMicrokernel;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Punto de entrada de la aplicación de escritorio (Swing).
 *
 * Es la capa de presentación: arma la solicitud (QuestionRequest) a
 * partir de lo que el usuario diligencia en el formulario, y delega
 * toda la lógica de generación y validación al QuestionMicrokernel.
 * Esta clase nunca conoce los plugins concretos ni los filtros: solo
 * conoce el núcleo.
 */
public class Main extends JFrame {

    private final QuestionMicrokernel microkernel;

    private JComboBox<String> cmbTipo;
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JComboBox<String> cmbClasificacion;
    private JTextField txtOpcion1;
    private JTextField txtOpcion2;
    private JTextField txtOpcion3;
    private JTextField txtOpcion4;
    private JTextField txtRespuestaCorrecta;
    private JLabel lblAyudaOpciones;

    private DefaultTableModel modeloTabla;
    private JTable tablaPreguntas;

    public Main() {
        this.microkernel = new QuestionMicrokernel();
        inicializarVentana();
        crearComponentes();
    }

    private void inicializarVentana() {
        setTitle("Banco de Preguntas Saber Pro — Microkernel + Pipes & Filters");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel(
                "Generador de Preguntas (arquitectura Microkernel)", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        principal.add(titulo, BorderLayout.NORTH);

        principal.add(crearFormulario(), BorderLayout.WEST);
        principal.add(crearPanelTabla(), BorderLayout.CENTER);

        setContentPane(principal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva pregunta"));

        cmbTipo = new JComboBox<>(new String[]{"MULTIPLE_CHOICE", "CASE_STUDY", "MULTIMEDIA"});
        cmbTipo.addActionListener(e -> actualizarVisibilidadOpciones());

        txtTitulo = new JTextField();
        txtContenido = new JTextArea(4, 20);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);

        List<String> clasificaciones = ClassificationFilter.getValidClassifications()
                .stream().sorted().toList();
        cmbClasificacion = new JComboBox<>(clasificaciones.toArray(new String[0]));

        txtOpcion1 = new JTextField();
        txtOpcion2 = new JTextField();
        txtOpcion3 = new JTextField();
        txtOpcion4 = new JTextField();
        txtRespuestaCorrecta = new JTextField();

        lblAyudaOpciones = new JLabel(
                "<html><i>Solo aplica a Selección Múltiple (4 opciones exactas).</i></html>");
        lblAyudaOpciones.setFont(new Font("Arial", Font.PLAIN, 10));

        panel.add(campo("Tipo de pregunta:", cmbTipo));
        panel.add(campo("Título:", txtTitulo));
        panel.add(campo("Contenido:", new JScrollPane(txtContenido)));
        panel.add(campo("Área de conocimiento:", cmbClasificacion));
        panel.add(lblAyudaOpciones);
        panel.add(campo("Opción 1:", txtOpcion1));
        panel.add(campo("Opción 2:", txtOpcion2));
        panel.add(campo("Opción 3:", txtOpcion3));
        panel.add(campo("Opción 4:", txtOpcion4));
        panel.add(campo("Respuesta correcta:", txtRespuestaCorrecta));

        JButton btnGenerar = new JButton("Generar pregunta");
        btnGenerar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenerar.addActionListener(e -> generarPregunta());
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnGenerar);

        return panel;
    }

    private JPanel campo(String etiqueta, JComponent componente) {
        JPanel fila = new JPanel(new BorderLayout(5, 2));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        fila.add(new JLabel(etiqueta), BorderLayout.NORTH);
        fila.add(componente, BorderLayout.CENTER);
        return fila;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Banco de preguntas (Map<String, Question>)"));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Título", "Contenido"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPreguntas = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaPreguntas), BorderLayout.CENTER);

        return panel;
    }

    private void actualizarVisibilidadOpciones() {
        boolean esMultipleChoice = "MULTIPLE_CHOICE".equals(cmbTipo.getSelectedItem());
        txtOpcion1.setEnabled(esMultipleChoice);
        txtOpcion2.setEnabled(esMultipleChoice);
        txtOpcion3.setEnabled(esMultipleChoice);
        txtOpcion4.setEnabled(esMultipleChoice);
        txtRespuestaCorrecta.setEnabled(esMultipleChoice);
    }

    private void generarPregunta() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        String contenido = txtContenido.getText().trim();
        String clasificacion = (String) cmbClasificacion.getSelectedItem();

        List<String> opciones = "MULTIPLE_CHOICE".equals(tipo)
                ? Arrays.asList(
                        txtOpcion1.getText().trim(),
                        txtOpcion2.getText().trim(),
                        txtOpcion3.getText().trim(),
                        txtOpcion4.getText().trim())
                : null;

        String respuestaCorrecta = "MULTIPLE_CHOICE".equals(tipo)
                ? txtRespuestaCorrecta.getText().trim()
                : null;

        QuestionRequest request = new QuestionRequest(
                titulo, contenido, tipo, clasificacion, opciones, respuestaCorrecta);

        int totalAntes = microkernel.getQuestions().size();

        try {
            microkernel.executePlugin(tipo, request);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalDespues = microkernel.getQuestions().size();

        if (totalDespues == totalAntes) {
            JOptionPane.showMessageDialog(this,
                    "La solicitud no superó las validaciones del pipeline de filtros.\n"
                            + "Revisa la consola para ver en qué filtro falló.",
                    "Validación fallida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Pregunta generada y agregada al banco correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        refrescarTabla();
        limpiarFormulario();
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Question q : microkernel.getQuestions().values()) {
            modeloTabla.addRow(new Object[]{q.getId(), q.getType(), q.getTitle(), q.getContent()});
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtContenido.setText("");
        txtOpcion1.setText("");
        txtOpcion2.setText("");
        txtOpcion3.setText("");
        txtOpcion4.setText("");
        txtRespuestaCorrecta.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main ventana = new Main();
            ventana.setVisible(true);
        });
    }
}
