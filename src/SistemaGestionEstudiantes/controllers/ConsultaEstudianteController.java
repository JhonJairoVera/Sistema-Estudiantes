package SistemaGestionEstudiantes.controllers;

import SistemaGestionEstudiantes.Estudiante;
import SistemaGestionEstudiantes.Materia;
import SistemaGestionEstudiantes.database.GestorEstudiante;
import SistemaGestionEstudiantes.GestorMaterias;
import SistemaGestionEstudiantes.GestorNotas;
import SistemaGestionEstudiantes.database.Nota;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaEstudianteController {

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextArea areaResultado;

    private GestorEstudiante gestorEstudiantes = new GestorEstudiante();
    private GestorMaterias gestorMaterias = new GestorMaterias();
    private GestorNotas gestorNotas = new GestorNotas();

    @FXML
    private void initialize() {
        System.out.println("Controlador de consulta inicializado");
        areaResultado.setText("Sistema de consulta de notas\n\n" +
                "Ingrese su número de identificación y presione Buscar");
    }

    @FXML
    private void buscarEstudiante() {
        String identificacion = txtIdentificacion.getText().trim();

        if (identificacion.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un número de identificación", Alert.AlertType.WARNING);
            return;
        }

        // Buscar estudiante por identificación
        Estudiante estudiante = null;
        List<Estudiante> estudiantes = gestorEstudiantes.getEstudiantes();

        System.out.println("Buscando estudiante con identificación: " + identificacion);
        System.out.println("Total estudiantes en sistema: " + estudiantes.size());

        for (Estudiante e : estudiantes) {
            System.out.println("Estudiante ID: " + e.getId() + ", Identificación: " + e.getIdentificacion());
            if (e.getIdentificacion().equals(identificacion)) {
                estudiante = e;
                break;
            }
        }

        if (estudiante == null) {
            mostrarAlerta("Error", "No se encontró un estudiante con la identificación: " + identificacion,
                    Alert.AlertType.ERROR);
            areaResultado.setText(" No se encontró ningún estudiante con la identificación:\n" + identificacion);
            return;
        }

        // Obtener todas las notas del sistema
        ArrayList<Nota> todasNotas = gestorNotas.obtenerNotas();
        System.out.println("Total notas en sistema: " + todasNotas.size());

        // Filtrar notas del estudiante
        ArrayList<Nota> notasEstudiante = new ArrayList<>();
        for (Nota nota : todasNotas) {
            if (nota.getIdEstudiante() == estudiante.getId()) {
                notasEstudiante.add(nota);
            }
        }

        System.out.println("Notas encontradas para el estudiante: " + notasEstudiante.size());

        if (notasEstudiante.isEmpty()) {
            String mensajeSinNotas = "=== CONSULTA DE NOTAS ===\n\n" +
                    "👤 ESTUDIANTE:\n" +
                    "Identificación: " + estudiante.getIdentificacion() + "\n" +
                    "Nombre: " + estudiante.getNombre() + "\n" +
                    "ID Interno: EST" + String.format("%03d", estudiante.getId()) + "\n\n" +
                    "NOTAS: \n" +
                    "El estudiante no tiene notas registradas.\n";
            areaResultado.setText(mensajeSinNotas);
            return;
        }

        // Construir resultado con datos reales
        String resultado = construirResultado(estudiante, notasEstudiante);
        areaResultado.setText(resultado);
    }

    private String construirResultado(Estudiante estudiante, ArrayList<Nota> notasEstudiante) {
        StringBuilder resultado = new StringBuilder();

        // Encabezado
        resultado.append("=== CONSULTA DE NOTAS ===\n\n");
        resultado.append("👤 ESTUDIANTE:\n");
        resultado.append("Identificación: ").append(estudiante.getIdentificacion()).append("\n");
        resultado.append("Nombre: ").append(estudiante.getNombre()).append("\n");
        resultado.append("ID: ").append(estudiante.getId()).append("\n\n");

        resultado.append("NOTAS POR MATERIA:\n\n");

        // Obtener materias para mostrar nombres
        List<Materia> materias = gestorMaterias.getMaterias();

        // Agrupar notas por materia
        Map<String, ArrayList<Double>> notasPorMateria = new HashMap<>();

        for (Nota nota : notasEstudiante) {
            String nombreMateria = "Materia " + nota.getIdMateria();

            // Buscar nombre de la materia
            for (Materia materia : materias) {
                if (materia.getId() == nota.getIdMateria()) {
                    nombreMateria = materia.getNombre();
                    break;
                }
            }

            if (!notasPorMateria.containsKey(nombreMateria)) {
                notasPorMateria.put(nombreMateria, new ArrayList<>());
            }
            notasPorMateria.get(nombreMateria).add(nota.getNota());
        }

        // Mostrar notas por materia
        for (Map.Entry<String, ArrayList<Double>> entry : notasPorMateria.entrySet()) {
            resultado.append(entry.getKey()).append(":\n");

            int contador = 1;
            double suma = 0;
            for (Double nota : entry.getValue()) {
                resultado.append("  • Nota ").append(contador).append(": ")
                        .append(String.format("%.2f", nota)).append("\n");
                suma += nota;
                contador++;
            }

            double promedio = suma / entry.getValue().size();
            resultado.append("  Promedio: ").append(String.format("%.2f", promedio)).append("\n\n");
        }

        // Calcular promedio general
        double promedioGeneral = 0;
        int totalNotas = 0;
        for (ArrayList<Double> notas : notasPorMateria.values()) {
            for (Double nota : notas) {
                promedioGeneral += nota;
                totalNotas++;
            }
        }

        if (totalNotas > 0) {
            promedioGeneral /= totalNotas;
            resultado.append("🎯 PROMEDIO GENERAL: ").append(String.format("%.2f", promedioGeneral)).append("\n\n");

            // Estado (aprobado/reprobado)
            String estado = (promedioGeneral >= 3.0) ? "APROBADO ✓" : "REPROBADO ✗";
            resultado.append("Estado: ").append(estado);
        }

        return resultado.toString();
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