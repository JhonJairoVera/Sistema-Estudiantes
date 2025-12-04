package SistemaGestionEstudiantes.controllers;

import SistemaGestionEstudiantes.Estudiante;
import SistemaGestionEstudiantes.Materia;
import SistemaGestionEstudiantes.database.GestorEstudiante;
import SistemaGestionEstudiantes.GestorMaterias;
import SistemaGestionEstudiantes.GestorNotas;
import SistemaGestionEstudiantes.database.Nota;
import SistemaGestionEstudiantes.GestorContrasena;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuProfesorController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtBuscar;
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colIdentificacion;
    @FXML private TableColumn<Estudiante, String> colNombre;

    @FXML private ComboBox<Estudiante> comboEstudiantes;
    @FXML private ComboBox<Materia> comboMaterias;
    @FXML private TextField txtNota;

    @FXML private TextField txtNuevaMateria;
    @FXML private ListView<Materia> listaMaterias;

    @FXML private PasswordField txtNuevaContrasena;

    private GestorEstudiante gestorEstudiantes = new GestorEstudiante();
    private GestorMaterias gestorMaterias = new GestorMaterias();
    private GestorNotas gestorNotas = new GestorNotas();

    @FXML
    public void initialize() {
        System.out.println("MenuProfesorController inicializado");

        // Configurar tabla de estudiantes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        cargarEstudiantesEnTabla();
        cargarEstudiantesEnComboBox();
        cargarMateriasEnComboBox();
        cargarMateriasEnListView();
    }

    // ================ MÉTODOS PARA ESTUDIANTES ================

    @FXML
    private void agregarEstudiante() {
        String identificacion = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (identificacion.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        // Verificar si ya existe estudiante con esa identificación
        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
            if (e.getIdentificacion().equals(identificacion)) {
                mostrarAlerta("Error", "Ya existe un estudiante con esa identificación", Alert.AlertType.ERROR);
                return;
            }
        }

        // Agregar nuevo estudiante usando el método de GestorEstudiante
        String resultado = gestorEstudiantes.agregarEstudiante(identificacion, nombre);

        if (resultado != null) {
            mostrarAlerta("Éxito", "Estudiante agregado correctamente con ID: " + resultado, Alert.AlertType.INFORMATION);
            txtIdentificacion.clear();
            txtNombre.clear();
            cargarEstudiantesEnTabla();
            cargarEstudiantesEnComboBox();
        } else {
            mostrarAlerta("Error", "No se pudo agregar el estudiante", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void buscarEstudiante() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            cargarEstudiantesEnTabla();
            return;
        }

        ObservableList<Estudiante> estudiantesFiltrados = FXCollections.observableArrayList();

        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
            if (e.getNombre().toLowerCase().contains(busqueda) ||
                    e.getIdentificacion().toLowerCase().contains(busqueda)) {
                estudiantesFiltrados.add(e);
            }
        }

        tablaEstudiantes.setItems(estudiantesFiltrados);
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
        confirmacion.setContentText("Se eliminará: " + seleccionado.getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Convertir int id a String para el método eliminarEstudiante
                boolean eliminado = gestorEstudiantes.eliminarEstudiante(String.valueOf(seleccionado.getId()));

                if (eliminado) {
                    mostrarAlerta("Éxito", "Estudiante eliminado", Alert.AlertType.INFORMATION);
                    cargarEstudiantesEnTabla();
                    cargarEstudiantesEnComboBox();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el estudiante", Alert.AlertType.ERROR);
                }
            }
        });
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

        // Filtrar notas del estudiante seleccionado
        ArrayList<Nota> notasEstudiante = new ArrayList<>();
        for (Nota nota : todasNotas) {
            if (nota.getIdEstudiante() == estudianteSeleccionado.getId()) {
                notasEstudiante.add(nota);
            }
        }

        if (notasEstudiante.isEmpty()) {
            mostrarAlerta("Información",
                    "El estudiante " + estudianteSeleccionado.getNombre() +
                            " no tiene notas registradas.",
                    Alert.AlertType.INFORMATION);
            return;
        }

        // Crear ventana con tabla
        crearVentanaNotasTabla(estudianteSeleccionado, notasEstudiante);
    }

    private void crearVentanaNotasTabla(Estudiante estudiante, ArrayList<Nota> notasEstudiante) {
        try {
            // Crear nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Notas de " + estudiante.getNombre());

            // Crear contenedor principal
            VBox root = new VBox(20);
            root.setStyle("-fx-padding: 20; -fx-background-color: #1D1D47;");

            // Título
            Label titulo = new Label("📊 NOTAS DEL ESTUDIANTE");
            titulo.setStyle("-fx-text-fill: #73FFFF; -fx-font-size: 24px; -fx-font-weight: bold;");

            // Información del estudiante
            VBox infoBox = new VBox(10);
            infoBox.setStyle("-fx-padding: 15; -fx-background-color: #000000; -fx-background-radius: 10;");

            GridPane infoGrid = new GridPane();
            infoGrid.setHgap(20);
            infoGrid.setVgap(10);

            Label lblNombre = new Label("Nombre:");
            lblNombre.setStyle("-fx-text-fill: white;");
            Label lblNombreValor = new Label(estudiante.getNombre());
            lblNombreValor.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Label lblIdentificacion = new Label("Identificación:");
            lblIdentificacion.setStyle("-fx-text-fill: white;");
            Label lblIdentificacionValor = new Label(estudiante.getIdentificacion());
            lblIdentificacionValor.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Label lblId = new Label("ID:");
            lblId.setStyle("-fx-text-fill: white;");
            Label lblIdValor = new Label(String.valueOf(estudiante.getId()));
            lblIdValor.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            infoGrid.add(lblNombre, 0, 0);
            infoGrid.add(lblNombreValor, 1, 0);
            infoGrid.add(lblIdentificacion, 2, 0);
            infoGrid.add(lblIdentificacionValor, 3, 0);
            infoGrid.add(lblId, 0, 1);
            infoGrid.add(lblIdValor, 1, 1);

            // Crear título para la sección de información
            Label infoTitulo = new Label("📋 INFORMACIÓN DEL ESTUDIANTE");
            infoTitulo.setStyle("-fx-text-fill: #73FFFF; -fx-font-size: 18px; -fx-font-weight: bold;");

            infoBox.getChildren().addAll(infoTitulo, infoGrid);

            // Crear tabla
            VBox tablaBox = new VBox(10);
            tablaBox.setStyle("-fx-padding: 15; -fx-background-color: #000000; -fx-background-radius: 10;");

            Label tablaTitulo = new Label("📊 NOTAS REGISTRADAS");
            tablaTitulo.setStyle("-fx-text-fill: #73FFFF; -fx-font-size: 18px; -fx-font-weight: bold;");

            // Crear tabla
            TableView<NotaMateria> tabla = new TableView<>();
            tabla.setPrefHeight(300);
            tabla.setStyle("-fx-background-color: #2d2d2d; -fx-control-inner-background: #2d2d2d;");

            // Columnas
            TableColumn<NotaMateria, String> colMateria = new TableColumn<>("MATERIA");
            colMateria.setPrefWidth(200);
            colMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));

            TableColumn<NotaMateria, String> colNotas = new TableColumn<>("NOTAS");
            colNotas.setPrefWidth(250);
            colNotas.setCellValueFactory(new PropertyValueFactory<>("notas"));

            TableColumn<NotaMateria, String> colPromedio = new TableColumn<>("PROMEDIO");
            colPromedio.setPrefWidth(100);
            colPromedio.setCellValueFactory(new PropertyValueFactory<>("promedio"));

            TableColumn<NotaMateria, String> colEstado = new TableColumn<>("ESTADO");
            colEstado.setPrefWidth(100);
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

            // Aplicar estilos a las celdas
            configurarEstiloCeldas(colMateria, colNotas, colPromedio, colEstado);

            tabla.getColumns().addAll(colMateria, colNotas, colPromedio, colEstado);

            // Obtener datos para la tabla
            ObservableList<NotaMateria> listaNotas = procesarNotasParaTabla(estudiante, notasEstudiante);
            tabla.setItems(listaNotas);

            // Calcular promedio general usando GestorEstudiante
            double promedioGeneral = gestorEstudiantes.promedioGeneral(estudiante.getId());

            HBox promedioBox = new HBox(20);
            promedioBox.setAlignment(Pos.CENTER_RIGHT);

            Label lblPromedio = new Label("PROMEDIO GENERAL:");
            lblPromedio.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

            Label lblPromedioValor = new Label();
            Label lblEstado = new Label();

            lblPromedioValor.setText(String.format("%.2f", promedioGeneral));

            if (promedioGeneral >= 3.5) {
                lblPromedioValor.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 18px;");
                lblEstado.setText("APROBADO ✓");
                lblEstado.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 16px;");
            } else if (promedioGeneral >= 3.0) {
                lblPromedioValor.setStyle("-fx-text-fill: #FFC107; -fx-font-weight: bold; -fx-font-size: 18px;");
                lblEstado.setText("APROBADO ✓");
                lblEstado.setStyle("-fx-text-fill: #FFC107; -fx-font-weight: bold; -fx-font-size: 16px;");
            } else {
                lblPromedioValor.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 18px;");
                lblEstado.setText("REPROBADO ✗");
                lblEstado.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 16px;");
            }

            promedioBox.getChildren().addAll(lblPromedio, lblPromedioValor, lblEstado);

            tablaBox.getChildren().addAll(tablaTitulo, tabla, promedioBox);

            // Botón cerrar
            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 40; -fx-pref-width: 120;");
            btnCerrar.setOnAction(e -> stage.close());

            // Agregar todo al root
            root.getChildren().addAll(titulo, infoBox, tablaBox, btnCerrar);

            // Mostrar ventana
            Scene scene = new Scene(root, 700, 600);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo crear la ventana de notas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<NotaMateria> procesarNotasParaTabla(Estudiante estudiante, ArrayList<Nota> notasEstudiante) {
        ObservableList<NotaMateria> listaNotas = FXCollections.observableArrayList();
        List<Materia> materias = gestorMaterias.getMaterias();
        Map<String, ArrayList<Double>> notasPorMateria = new HashMap<>();

        // Agrupar notas por materia
        for (Nota nota : notasEstudiante) {
            String nombreMateria = "Materia " + nota.getIdMateria();
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

        // Crear objetos NotaMateria para la tabla
        for (Map.Entry<String, ArrayList<Double>> entry : notasPorMateria.entrySet()) {
            StringBuilder notasTexto = new StringBuilder();
            double sumaMateria = 0;

            for (int i = 0; i < entry.getValue().size(); i++) {
                Double nota = entry.getValue().get(i);
                notasTexto.append(String.format("%.1f", nota));
                if (i < entry.getValue().size() - 1) notasTexto.append(" | ");
                sumaMateria += nota;
            }

            double promedioMateria = sumaMateria / entry.getValue().size();
            String estado = (promedioMateria >= 3.0) ? "APROBADO ✓" : "REPROBADO ✗";

            listaNotas.add(new NotaMateria(
                    entry.getKey(),
                    notasTexto.toString(),
                    String.format("%.2f", promedioMateria),
                    estado
            ));
        }

        return listaNotas;
    }

    private void configurarEstiloCeldas(
            TableColumn<NotaMateria, String> colMateria,
            TableColumn<NotaMateria, String> colNotas,
            TableColumn<NotaMateria, String> colPromedio,
            TableColumn<NotaMateria, String> colEstado) {

        colMateria.setCellFactory(column -> new TableCell<NotaMateria, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-alignment: CENTER_LEFT; -fx-padding: 5;");
                }
            }
        });

        colNotas.setCellFactory(column -> new TableCell<NotaMateria, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                }
            }
        });

        colPromedio.setCellFactory(column -> new TableCell<NotaMateria, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    try {
                        double valor = Double.parseDouble(item);
                        if (valor >= 3.5) {
                            setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                        } else if (valor >= 3.0) {
                            setStyle("-fx-text-fill: #FFC107; -fx-font-weight: bold; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                        } else {
                            setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                        }
                    } catch (NumberFormatException e) {
                        setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                    }
                }
            }
        });

        colEstado.setCellFactory(column -> new TableCell<NotaMateria, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("APROBADO") || item.contains("✓")) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                    } else {
                        setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 13px; -fx-alignment: CENTER; -fx-padding: 5;");
                    }
                }
            }
        });
    }

    // ================ MÉTODOS PARA NOTAS ================

    @FXML
    private void agregarNota() {
        Estudiante estudiante = comboEstudiantes.getValue();
        Materia materia = comboMaterias.getValue();
        String notaTexto = txtNota.getText().trim();

        if (estudiante == null || materia == null || notaTexto.isEmpty()) {
            mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        try {
            double nota = Double.parseDouble(notaTexto);

            if (nota < 0 || nota > 5) {
                mostrarAlerta("Error", "La nota debe estar entre 0 y 5", Alert.AlertType.ERROR);
                return;
            }

            // Usar GestorEstudiante para agregar nota (según tu implementación)
            boolean agregada = gestorEstudiantes.agregarNota(
                    String.valueOf(estudiante.getId()),
                    materia.getNombre(),
                    nota
            );

            if (agregada) {
                mostrarAlerta("Éxito", "Nota agregada correctamente", Alert.AlertType.INFORMATION);
                txtNota.clear();
            } else {
                mostrarAlerta("Error", "No se pudo agregar la nota", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarNota() {
        // Para eliminar nota, necesitarías seleccionar una nota específica
        // Por ahora, mostramos un mensaje informativo
        mostrarAlerta("Funcionalidad", "Para eliminar una nota específica, seleccione una nota de la lista", Alert.AlertType.INFORMATION);
    }

    // ================ MÉTODOS PARA MATERIAS ================

    @FXML
    private void agregarMateria() {
        String nombre = txtNuevaMateria.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Error", "Ingrese el nombre de la materia", Alert.AlertType.ERROR);
            return;
        }

        // Agregar materia usando GestorMaterias
        boolean agregada = gestorMaterias.agregarMateria(nombre);

        if (agregada) {
            mostrarAlerta("Éxito", "Materia agregada correctamente", Alert.AlertType.INFORMATION);
            txtNuevaMateria.clear();
            cargarMateriasEnComboBox();
            cargarMateriasEnListView();
        } else {
            mostrarAlerta("Error", "La materia ya existe o no se pudo agregar", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarMateria() {
        Materia seleccionada = listaMaterias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Error", "Seleccione una materia de la lista", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar materia?");
        confirmacion.setContentText("Se eliminará: " + seleccionada.getNombre());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean eliminada = gestorMaterias.eliminarMateria(seleccionada.getId());

                if (eliminada) {
                    mostrarAlerta("Éxito", "Materia eliminada", Alert.AlertType.INFORMATION);
                    cargarMateriasEnComboBox();
                    cargarMateriasEnListView();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar la materia", Alert.AlertType.ERROR);
                }
            }
        });
    }

    // ================ MÉTODOS DE CONFIGURACIÓN ================

    @FXML
    private void cambiarContrasena() {
        String nuevaContrasena = txtNuevaContrasena.getText().trim();

        if (nuevaContrasena.length() != 4 || !nuevaContrasena.matches("\\d{4}")) {
            mostrarAlerta("Error", "La contraseña debe tener 4 dígitos numéricos", Alert.AlertType.ERROR);
            return;
        }

        // Usar el método correcto de GestorContrasena
        GestorContrasena.cambiarContraseña(nuevaContrasena);
        mostrarAlerta("Éxito", "Contraseña cambiada correctamente", Alert.AlertType.INFORMATION);
        txtNuevaContrasena.clear();
    }

    @FXML
    private void volverAlMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuPrincipal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Gestión Estudiantil");

            // Cerrar ventana actual
            Stage stageActual = (Stage) txtIdentificacion.getScene().getWindow();
            stageActual.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar el menú principal", Alert.AlertType.ERROR);
        }
    }

    // ================ MÉTODOS AUXILIARES ================

    private void cargarEstudiantesEnTabla() {
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(gestorEstudiantes.getEstudiantes());
        tablaEstudiantes.setItems(estudiantes);
    }

    private void cargarEstudiantesEnComboBox() {
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(gestorEstudiantes.getEstudiantes());
        comboEstudiantes.setItems(estudiantes);
    }

    private void cargarMateriasEnComboBox() {
        ObservableList<Materia> materias = FXCollections.observableArrayList(gestorMaterias.getMaterias());
        comboMaterias.setItems(materias);
    }

    private void cargarMateriasEnListView() {
        ObservableList<Materia> materias = FXCollections.observableArrayList(gestorMaterias.getMaterias());
        listaMaterias.setItems(materias);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}