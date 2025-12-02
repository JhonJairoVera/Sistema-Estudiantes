package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConsultaEstudianteController {

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextArea areaResultado;

    @FXML
    private void initialize() {
        System.out.println("✅ Controlador de consulta inicializado");
        areaResultado.setText("Sistema de consulta de notas\n\n" +
                "Ingrese su número de identificación y presione Buscar");
    }

    @FXML
    private void buscarEstudiante() {
        String id = txtIdentificacion.getText().trim();

        if (id.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un número de identificación", Alert.AlertType.WARNING);
            return;
        }

        // Esto es TEMPORAL - después pondrás tu lógica real
        String resultado = obtenerDatosEjemplo(id);
        areaResultado.setText(resultado);
    }

    private String obtenerDatosEjemplo(String id) {
        return "=== CONSULTA DE NOTAS ===\n\n" +
                "👤 ESTUDIANTE:\n" +
                "Identificación: " + id + "\n" +
                "Nombre: Juan Pérez\n" +
                "ID Interno: EST001\n\n" +
                "📊 NOTAS POR MATERIA:\n\n" +
                "Matemáticas:\n" +
                "  • Nota 1: 4.5\n" +
                "  • Nota 2: 3.8\n" +
                "  • Nota 3: 4.0\n" +
                "  Promedio: 4.10\n\n" +
                "Física:\n" +
                "  • Nota 1: 3.2\n" +
                "  • Nota 2: 3.5\n" +
                "  • Nota 3: 4.0\n" +
                "  Promedio: 3.57\n\n" +
                "Programación:\n" +
                "  • Nota 1: 4.8\n" +
                "  • Nota 2: 4.5\n" +
                "  • Nota 3: 5.0\n" +
                "  Promedio: 4.77\n\n" +
                "🎯 PROMEDIO GENERAL: 4.15\n\n" +
                "Estado: APROBADO ✓";
    }

    @FXML
    private void nuevaBusqueda() {
        txtIdentificacion.clear();
        areaResultado.clear();
        areaResultado.setText("Ingrese un nuevo número de identificación...");
        txtIdentificacion.requestFocus();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtIdentificacion.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}