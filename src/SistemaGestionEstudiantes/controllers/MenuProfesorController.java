package SistemaGestionEstudiantes.controllers;

import SistemaGestionEstudiantes.GestorNotas;
import SistemaGestionEstudiantes.database.Nota;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.stage.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import SistemaGestionEstudiantes.database.GestorEstudiante;
import SistemaGestionEstudiantes.GestorMaterias;
import SistemaGestionEstudiantes.GestorContrasena;
import SistemaGestionEstudiantes.Estudiante;
import SistemaGestionEstudiantes.Materia;

import java.io.IOException;
import java.util.List;

public class MenuProfesorController {

    // Estudiantes
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> colId;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colIdentificacion;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtBuscar;

    // Notas
    @FXML private ComboBox<Estudiante> comboEstudiantes;
    @FXML private ComboBox<String> comboMaterias;
    @FXML private TextField txtNota;

    // Materias
    @FXML private TextField txtNuevaMateria;
    @FXML private ListView<String> listaMaterias;

    // Contraseña
    @FXML private PasswordField txtNuevaContrasena;

    private GestorEstudiante gestorEstudiantes = new GestorEstudiante();
    private GestorMaterias gestorMaterias = new GestorMaterias();
    private GestorNotas gestorNotas = new GestorNotas();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asString());

        colIdentificacion.setCellValueFactory(cellData ->
                cellData.getValue().identificacionProperty());

        colNombre.setCellValueFactory(cellData ->
                cellData.getValue().nombreProperty());
    }

    private void cargarDatos() {
        // Cargar estudiantes en tabla
        List<Estudiante> estudiantes = gestorEstudiantes.getEstudiantes();
        ObservableList<Estudiante> listaEst = FXCollections.observableArrayList(estudiantes);
        tablaEstudiantes.setItems(listaEst);

        // Cargar estudiantes en ComboBox
        comboEstudiantes.setItems(listaEst);
        comboEstudiantes.setCellFactory(param -> new ListCell<Estudiante>() {
            @Override
            protected void updateItem(Estudiante item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " (ID: " + item.getId() + ")");
                }
            }
        });

        // Cargar materias
        List<Materia> materias = gestorMaterias.getMaterias();
        ObservableList<String> nombresMaterias = FXCollections.observableArrayList();
        for (Materia m : materias) {
            nombresMaterias.add(m.getNombre());
        }
        comboMaterias.setItems(nombresMaterias);
        listaMaterias.setItems(nombresMaterias);
    }

    @FXML
    private void agregarEstudiante() {
        String identificacion = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (identificacion.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.WARNING);
            return;
        }

        String idGenerado = gestorEstudiantes.agregarEstudiante(identificacion, nombre);

        if (idGenerado != null) {
            mostrarAlerta("Éxito", "Estudiante agregado con ID: " + idGenerado, Alert.AlertType.INFORMATION);
            cargarDatos();
            limpiarCamposEstudiante();
        } else {
            mostrarAlerta("Error", "Error al agregar estudiante", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void buscarEstudiante() {
        String texto = txtBuscar.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            cargarDatos();
            return;
        }

        ObservableList<Estudiante> filtrados = FXCollections.observableArrayList();
        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
            if (e.getNombre().toLowerCase().contains(texto) ||
                    e.getIdentificacion().toLowerCase().contains(texto) ||
                    String.valueOf(e.getId()).contains(texto)) {
                filtrados.add(e);
            }
        }

        tablaEstudiantes.setItems(filtrados);
    }

    @FXML
    private void eliminarEstudiante() {
        Estudiante seleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un estudiante de la tabla", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar estudiante?");
        confirmacion.setContentText("¿Está seguro de eliminar a " + seleccionado.getNombre() + "?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (gestorEstudiantes.eliminarEstudiante(String.valueOf(seleccionado.getId()))) {
                    mostrarAlerta("Éxito", "Estudiante eliminado", Alert.AlertType.INFORMATION);
                    cargarDatos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el estudiante", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void agregarNota() {
        Estudiante estudiante = comboEstudiantes.getValue();
        String materia = comboMaterias.getValue();
        String notaTexto = txtNota.getText().trim();

        if (estudiante == null || materia == null || notaTexto.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.WARNING);
            return;
        }

        try {
            double nota = Double.parseDouble(notaTexto);

            if (nota < 0 || nota > 5) {
                mostrarAlerta("Error", "La nota debe estar entre 0 y 5", Alert.AlertType.ERROR);
                return;
            }

            if (gestorEstudiantes.agregarNota(
                    String.valueOf(estudiante.getId()), materia, nota)) {
                mostrarAlerta("Éxito", "Nota agregada correctamente", Alert.AlertType.INFORMATION);
                txtNota.clear();
            } else {
                mostrarAlerta("Error", "Error al agregar nota", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void agregarMateria() {
        String nuevaMateria = txtNuevaMateria.getText().trim();

        if (nuevaMateria.isEmpty()) {
            mostrarAlerta("Error", "Ingrese el nombre de la materia", Alert.AlertType.WARNING);
            return;
        }

        if (gestorMaterias.agregarMateria(nuevaMateria)) {
            mostrarAlerta("Éxito", "Materia agregada", Alert.AlertType.INFORMATION);
            txtNuevaMateria.clear();
            cargarDatos();
        } else {
            mostrarAlerta("Error", "La materia ya existe", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarMateria() {
        String materiaSeleccionada = listaMaterias.getSelectionModel().getSelectedItem();

        if (materiaSeleccionada == null) {
            mostrarAlerta("Error", "Seleccione una materia", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar materia?");
        confirmacion.setContentText("¿Está seguro de eliminar " + materiaSeleccionada + "?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                List<Materia> materias = gestorMaterias.getMaterias();
                int idMateria = -1;
                for (Materia m : materias) {
                    if (m.getNombre().equals(materiaSeleccionada)) {
                        idMateria = m.getId();
                        break;
                    }
                }

                if (idMateria != -1 && gestorMaterias.eliminarMateria(idMateria)) {
                    mostrarAlerta("Éxito", "Materia eliminada", Alert.AlertType.INFORMATION);
                    cargarDatos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar la materia", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void cambiarContrasena() {
        String nuevaContrasena = txtNuevaContrasena.getText().trim();

        if (nuevaContrasena.length() != 4 || !nuevaContrasena.matches("\\d{4}")) {
            mostrarAlerta("Error", "La contraseña debe tener 4 dígitos numéricos", Alert.AlertType.ERROR);
            return;
        }

        GestorContrasena.cambiarContraseña(nuevaContrasena);
        mostrarAlerta("Éxito", "Contraseña cambiada correctamente", Alert.AlertType.INFORMATION);
        txtNuevaContrasena.clear();
    }

    @FXML
    private void volverAlMenuPrincipal(ActionEvent event) {
        System.out.println("Volviendo al menú principal...");

        try {
            if (event != null && event.getSource() instanceof javafx.scene.Node) {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
            else if (tablaEstudiantes != null) {
                Stage stage = (Stage) tablaEstudiantes.getScene().getWindow();
                stage.close();
            }

            abrirVentanaMenuPrincipal();

        } catch (Exception e) {
            System.err.println("Error al volver al menú principal: " + e.getMessage());
            e.printStackTrace();
            cerrarVentanaActual();
        }
    }

    private void abrirVentanaMenuPrincipal() {
        try {
            System.out.println("Abriendo menú principal...");

            java.net.URL url = getClass().getResource("/views/MenuPrincipal.fxml");
            if (url == null) {
                System.err.println("ERROR: No se encuentra MenuPrincipal.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestión de Estudiantes");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

            System.out.println("Menú principal abierto exitosamente");

        } catch (IOException e) {
            System.err.println("Error al abrir menú principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cerrarVentanaActual() {
        try {
            for (Window window : Window.getWindows()) {
                if (window.isShowing() && window instanceof Stage) {
                    ((Stage) window).close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCamposEstudiante() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtBuscar.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void verNotasDelSeleccionado() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un estudiante de la tabla", Alert.AlertType.WARNING);
            return;
        }

        // Obtener todas las notas
        ArrayList<Nota> todasNotas = gestorNotas.obtenerNotas();

        // DEBUG: Verificar datos
        System.out.println("=== DEBUG ===");
        System.out.println("Estudiante seleccionado ID: " + estudianteSeleccionado.getId());
        System.out.println("Total notas en sistema: " + todasNotas.size());

        // Filtrar notas del estudiante seleccionado - CORRECCIÓN AQUÍ
        ArrayList<Nota> notasEstudiante = new ArrayList<>();
        for (Nota nota : todasNotas) {
            // COMPARACIÓN CORREGIDA: Comparar int con int
            if (nota.getIdEstudiante() == estudianteSeleccionado.getId()) {
                notasEstudiante.add(nota);
                System.out.println("Nota encontrada: " + nota.getId() + " - " + nota.getNota());
            }
        }

        if (notasEstudiante.isEmpty()) {
            mostrarAlerta("Información",
                    "El estudiante " + estudianteSeleccionado.getNombre() +
                            " no tiene notas registradas.",
                    Alert.AlertType.INFORMATION);
            return;
        }

        // Construir mensaje con las notas
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("=== NOTAS DE ").append(estudianteSeleccionado.getNombre().toUpperCase()).append(" ===\n\n");
        mensaje.append("ID: ").append(estudianteSeleccionado.getId()).append("\n");
        mensaje.append("Identificación: ").append(estudianteSeleccionado.getIdentificacion()).append("\n\n");

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
            mensaje.append(entry.getKey()).append(":\n");

            double suma = 0;
            for (Double nota : entry.getValue()) {
                mensaje.append("  • ").append(String.format("%.2f", nota)).append("\n");
                suma += nota;
            }

            double promedio = suma / entry.getValue().size();
            mensaje.append("  Promedio: ").append(String.format("%.2f", promedio)).append("\n\n");
        }

        // Mostrar promedio general
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
            mensaje.append("🎯 PROMEDIO GENERAL: ").append(String.format("%.2f", promedioGeneral));
        }

        // Mostrar diálogo con las notas
        TextArea textArea = new TextArea(mensaje.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(500, 400);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Notas del Estudiante");
        dialog.setHeaderText("Notas de: " + estudianteSeleccionado.getNombre());
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        dialog.showAndWait();
    }

    @FXML
    private void eliminarNota() {
        // Obtener todas las notas
        ArrayList<Nota> todasNotas = gestorNotas.obtenerNotas();

        if (todasNotas.isEmpty()) {
            mostrarAlerta("Información", "No hay notas registradas en el sistema.", Alert.AlertType.INFORMATION);
            return;
        }

        // Crear diálogo para seleccionar nota
        Dialog<Nota> dialog = new Dialog<>();
        dialog.setTitle("Eliminar Nota");
        dialog.setHeaderText("Seleccione la nota a eliminar:");

        // Crear lista de notas
        ListView<Nota> listView = new ListView<>();
        ObservableList<Nota> listaNotas = FXCollections.observableArrayList(todasNotas);
        listView.setItems(listaNotas);

        // Formatear cómo se muestran las notas
        listView.setCellFactory(param -> new ListCell<Nota>() {
            @Override
            protected void updateItem(Nota nota, boolean empty) {
                super.updateItem(nota, empty);
                if (empty || nota == null) {
                    setText(null);
                } else {
                    // Buscar estudiante
                    Estudiante estudiante = null;
                    for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                        if (e.getId() == nota.getIdEstudiante()) {  // CORRECCIÓN: Comparar int con int
                            estudiante = e;
                            break;
                        }
                    }

                    // Buscar materia
                    String nombreMateria = "Materia " + nota.getIdMateria();
                    for (Materia m : gestorMaterias.getMaterias()) {
                        if (m.getId() == nota.getIdMateria()) {
                            nombreMateria = m.getNombre();
                            break;
                        }
                    }

                    String nombreEstudiante = estudiante != null ? estudiante.getNombre() : "ID: " + nota.getIdEstudiante();
                    setText("ID Nota: " + nota.getId() + " | " + nombreEstudiante +
                            " | " + nombreMateria + " | Nota: " + String.format("%.2f", nota.getNota()));
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setPrefSize(600, 300);

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Configurar resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        // Mostrar diálogo y procesar resultado
        dialog.showAndWait().ifPresent(notaSeleccionada -> {
            if (notaSeleccionada != null) {
                // Confirmar eliminación
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Eliminación");
                confirmacion.setHeaderText("¿Está seguro de eliminar esta nota?");

                // Buscar información para el mensaje
                Estudiante estudiante = null;
                for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                    if (e.getId() == notaSeleccionada.getIdEstudiante()) {  // CORRECCIÓN: Comparar int con int
                        estudiante = e;
                        break;
                    }
                }

                String nombreMateria = "Materia " + notaSeleccionada.getIdMateria();
                for (Materia m : gestorMaterias.getMaterias()) {
                    if (m.getId() == notaSeleccionada.getIdMateria()) {
                        nombreMateria = m.getNombre();
                        break;
                    }
                }

                String mensajeConfirmacion = "Nota a eliminar:\n\n" +
                        "ID Nota: " + notaSeleccionada.getId() + "\n" +
                        "Estudiante: " + (estudiante != null ? estudiante.getNombre() : "ID: " + notaSeleccionada.getIdEstudiante()) + "\n" +
                        "Materia: " + nombreMateria + "\n" +
                        "Nota: " + String.format("%.2f", notaSeleccionada.getNota());

                confirmacion.setContentText(mensajeConfirmacion);

                confirmacion.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Eliminar la nota
                        if (gestorNotas.eliminarNota(notaSeleccionada.getId())) {
                            mostrarAlerta("Éxito", "Nota eliminada correctamente.", Alert.AlertType.INFORMATION);
                        } else {
                            mostrarAlerta("Error", "No se pudo eliminar la nota.", Alert.AlertType.ERROR);
                        }
                    }
                });
            }
        });
    }
}