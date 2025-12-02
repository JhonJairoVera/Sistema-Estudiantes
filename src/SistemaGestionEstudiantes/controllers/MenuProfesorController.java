package SistemaGestionEstudiantes.controllers;
// Añade estas importaciones si no las tienes:
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

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        // VERSIÓN 1: Usando .asString() (más limpio)
        colId.setCellValueFactory(cellData ->
                cellData.getValue().idProperty().asString());  // ¡IMPORTANTE: .asString()!

        colIdentificacion.setCellValueFactory(cellData ->
                cellData.getValue().identificacionProperty());

        colNombre.setCellValueFactory(cellData ->
                cellData.getValue().nombreProperty());

        /*
        // VERSIÓN 2: Usando SimpleStringProperty (si la primera no funciona)
        colId.setCellValueFactory(cellData -> {
            Estudiante e = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(e.getId()));
        });

        colIdentificacion.setCellValueFactory(cellData -> {
            Estudiante e = cellData.getValue();
            return new SimpleStringProperty(e.getIdentificacion());
        });

        colNombre.setCellValueFactory(cellData -> {
            Estudiante e = cellData.getValue();
            return new SimpleStringProperty(e.getNombre());
        });
        */
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

    private void volverAlMenuPrincipal(ActionEvent event) {  // ¡Recibe el event como parámetro!
        System.out.println("Volviendo al menú principal...");

        try {
            // FORMA CORRECTA: Usar el evento recibido como parámetro
            if (event != null && event.getSource() instanceof javafx.scene.Node) {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
            // FORMA ALTERNATIVA: Usar cualquier componente de la escena
            else if (tablaEstudiantes != null) {
                Stage stage = (Stage) tablaEstudiantes.getScene().getWindow();
                stage.close();
            }

            // Abrir menú principal
            abrirVentanaMenuPrincipal();

        } catch (Exception e) {
            System.err.println("Error al volver al menú principal: " + e.getMessage());
            e.printStackTrace();

            // Si todo falla, cerrar cualquier ventana visible
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
            // Cerrar la primera ventana que esté mostrando
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
}