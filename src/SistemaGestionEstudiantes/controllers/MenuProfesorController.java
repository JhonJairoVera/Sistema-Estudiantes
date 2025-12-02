package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// ¡IMPORTANTE! GestorEstudiante está en database
import SistemaGestionEstudiantes.database.GestorEstudiante;
import SistemaGestionEstudiantes.GestorMaterias;
import SistemaGestionEstudiantes.GestorNotas;

// Estudiante y Materia están en paquete raíz
import SistemaGestionEstudiantes.Estudiante;
import SistemaGestionEstudiantes.Materia;

// GestorContrasena está en paquete raíz
import SistemaGestionEstudiantes.GestorContrasena;

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
        // Configura las columnas
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colIdentificacion.setCellValueFactory(cellData -> cellData.getValue().identificacionProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
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
                setText(empty || item == null ? "" : item.getNombre() + " (" + item.getId() + ")");
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
        // Implementar
    }

    @FXML
    private void buscarEstudiante() {
        // Implementar
    }

    @FXML
    private void eliminarEstudiante() {
        // Implementar
    }

    @FXML
    private void agregarNota() {
        // Implementar
    }

    @FXML
    private void agregarMateria() {
        // Implementar
    }

    @FXML
    private void eliminarMateria() {
        // Implementar
    }

    @FXML
    private void cambiarContrasena() {
        // Implementar
    }

    @FXML
    private void volverAlMenuPrincipal() {
        // Implementar
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}