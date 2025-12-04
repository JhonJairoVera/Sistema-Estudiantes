package SistemaGestionEstudiantes.controllers;

import SistemaGestionEstudiantes.Estudiante;
import SistemaGestionEstudiantes.Materia;
import SistemaGestionEstudiantes.database.GestorEstudiante;
import SistemaGestionEstudiantes.GestorMaterias;
import SistemaGestionEstudiantes.GestorNotas;
import SistemaGestionEstudiantes.database.Nota;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaEstudianteController {

    @FXML private TextField txtIdentificacion;
    @FXML private VBox infoEstudiante;
    @FXML private Label lblNombre;
    @FXML private Label lblIdentificacion;
    @FXML private Label lblId;
    @FXML private VBox contenedorNotas;
    @FXML private TableView<NotaMateria> tablaNotas;
    @FXML private TableColumn<NotaMateria, String> colMateria;
    @FXML private TableColumn<NotaMateria, String> colNotas;
    @FXML private TableColumn<NotaMateria, String> colPromedio;
    @FXML private TableColumn<NotaMateria, String> colEstado;
    @FXML private Label lblPromedioGeneral;
    @FXML private Label lblEstadoGeneral;
    @FXML private VBox mensajeSinNotas;

    private GestorEstudiante gestorEstudiantes = new GestorEstudiante();
    private GestorMaterias gestorMaterias = new GestorMaterias();
    private GestorNotas gestorNotas = new GestorNotas();
    private ObservableList<NotaMateria> listaNotas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("✅ Controlador de consulta inicializado");

        // Configurar columnas
        colMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colNotas.setCellValueFactory(new PropertyValueFactory<>("notas"));
        colPromedio.setCellValueFactory(new PropertyValueFactory<>("promedio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        configurarEstiloCeldas();
        ocultarTodasLasSecciones();
        txtIdentificacion.requestFocus();
    }

    private void configurarEstiloCeldas() {
        // Configurar el cellFactory para cada columna
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
                    // Color condicional basado en el valor
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
    @FXML
    private void buscarEstudiante() {
        String identificacion = txtIdentificacion.getText().trim();

        if (identificacion.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un número de identificación", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Buscando estudiante con identificación: " + identificacion);

        // Buscar estudiante
        Estudiante estudiante = null;
        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
            if (e.getIdentificacion().equals(identificacion)) {
                estudiante = e;
                break;
            }
        }

        if (estudiante == null) {
            mostrarAlerta("Error", "No se encontró un estudiante con esa identificación", Alert.AlertType.ERROR);
            ocultarTodasLasSecciones();
            return;
        }

        // Mostrar información del estudiante
        mostrarInformacionEstudiante(estudiante);

        // Obtener notas del estudiante
        ArrayList<Nota> todasNotas = gestorNotas.obtenerNotas();
        ArrayList<Nota> notasEstudiante = new ArrayList<>();

        for (Nota nota : todasNotas) {
            if (nota.getIdEstudiante() == estudiante.getId()) {
                notasEstudiante.add(nota);
            }
        }

        if (notasEstudiante.isEmpty()) {
            mostrarMensajeSinNotas();
        } else {
            mostrarNotasEnTabla(estudiante, notasEstudiante);
        }
    }

    private void mostrarInformacionEstudiante(Estudiante estudiante) {
        infoEstudiante.setVisible(true);
        infoEstudiante.setOpacity(1);
        lblNombre.setText(estudiante.getNombre());
        lblIdentificacion.setText(estudiante.getIdentificacion());
        lblId.setText(String.valueOf(estudiante.getId()));
    }

    private void mostrarNotasEnTabla(Estudiante estudiante, ArrayList<Nota> notasEstudiante) {
        mensajeSinNotas.setVisible(false);
        contenedorNotas.setVisible(true);
        contenedorNotas.setOpacity(1);
        listaNotas.clear();

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

        // Calcular promedios y agregar a la tabla
        double totalGeneral = 0;
        int totalNotas = 0;

        for (Map.Entry<String, ArrayList<Double>> entry : notasPorMateria.entrySet()) {
            StringBuilder notasTexto = new StringBuilder();
            double sumaMateria = 0;

            for (int i = 0; i < entry.getValue().size(); i++) {
                Double nota = entry.getValue().get(i);
                notasTexto.append(String.format("%.1f", nota));
                if (i < entry.getValue().size() - 1) notasTexto.append(" | ");
                sumaMateria += nota;
                totalGeneral += nota;
                totalNotas++;
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

        tablaNotas.setItems(listaNotas);

        // Calcular promedio general
        if (totalNotas > 0) {
            double promedioGeneral = totalGeneral / totalNotas;
            lblPromedioGeneral.setText(String.format("%.2f", promedioGeneral));

            if (promedioGeneral >= 3.5) {
                lblPromedioGeneral.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 18px;");
            } else if (promedioGeneral >= 3.0) {
                lblPromedioGeneral.setStyle("-fx-text-fill: #FFC107; -fx-font-weight: bold; -fx-font-size: 18px;");
            } else {
                lblPromedioGeneral.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 18px;");
            }

            if (promedioGeneral >= 3.0) {
                lblEstadoGeneral.setText("APROBADO ✓");
                lblEstadoGeneral.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 16px;");
            } else {
                lblEstadoGeneral.setText("REPROBADO ✗");
                lblEstadoGeneral.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold; -fx-font-size: 16px;");
            }
        }
    }

    private void mostrarMensajeSinNotas() {
        contenedorNotas.setVisible(false);
        mensajeSinNotas.setVisible(true);
        mensajeSinNotas.setOpacity(1);
    }

    private void ocultarTodasLasSecciones() {
        infoEstudiante.setVisible(false);
        contenedorNotas.setVisible(false);
        mensajeSinNotas.setVisible(false);
    }

    @FXML
    private void nuevaBusqueda() {
        txtIdentificacion.clear();
        ocultarTodasLasSecciones();
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