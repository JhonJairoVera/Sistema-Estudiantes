package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import SistemaGestionEstudiantes.GestorContrasena;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MenuPrincipalController {

    @FXML private Button btnDocente;
    @FXML private Button btnEstudiante;
    @FXML private Button btnExtra;
    @FXML private Button btnCerrar;

    @FXML
    public void initialize() {
        System.out.println("MenuPrincipalController inicializado");
    }

    @FXML
    private void abrirMenuProfesor() {
        System.out.println("Intentando abrir menú profesor...");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Acceso Profesor");
        dialog.setHeaderText("Ingrese la contraseña de 4 dígitos");
        dialog.setContentText("Contraseña:");

        dialog.showAndWait().ifPresent(password -> {
            System.out.println("Contraseña ingresada: " + password);

            if (GestorContrasena.verificarContraseña(password)) {
                System.out.println("Contraseña correcta");
                cargarVentanaProfesor();
            } else {
                System.out.println("Contraseña incorrecta");
                mostrarError("Contraseña incorrecta. Use: 0000");
            }
        });
    }

    private void cargarVentanaProfesor() {
        try {
            System.out.println("Cargando MenuProfesor.fxml...");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/MenuProfesor.fxml"));
            Parent root = loader.load();

            System.out.println("FXML cargado exitosamente");

            Stage profesorStage = new Stage();
            profesorStage.setTitle("Menú del Profesor");
            profesorStage.setScene(new Scene(root, 900, 700));

            Stage stageActual = (Stage) btnDocente.getScene().getWindow();
            stageActual.close();

            profesorStage.show();
            System.out.println("Ventana del profesor mostrada");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar el menú del profesor: " + e.getMessage());
        } catch (NullPointerException e) {
            mostrarError("No se encuentra el archivo MenuProfesor.fxml");
        }
    }

    @FXML
    private void abrirMenuEstudiante() {
        System.out.println("Abriendo consulta de estudiante...");

        try {
            // Esto debe cargar el NUEVO archivo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ConsultaEstudianteView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Consulta de Notas - Estudiante");
            stage.setScene(new Scene(root, 700, 500));
            stage.show();

            System.out.println(" Ventana abierta correctamente");

        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearVentanaConsultaSimple() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Consulta de Estudiante");

            VBox root = new VBox(20);
            root.setStyle("-fx-padding: 30; -fx-background-color: #1D1D47;");

            Label titulo = new Label(" Consulta de Notas");
            titulo.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

            HBox hbox = new HBox(10);
            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Label lbl = new Label("Número de Identificación:");
            lbl.setStyle("-fx-text-fill: white;");

            TextField txtId = new TextField();
            txtId.setPromptText("Ingrese su CC");
            txtId.setPrefWidth(200);

            Button btnBuscar = new Button("Buscar");
            btnBuscar.setStyle("-fx-background-color: #73FFFF; -fx-text-fill: black;");

            TextArea areaResultado = new TextArea();
            areaResultado.setPrefHeight(300);
            areaResultado.setEditable(false);
            areaResultado.setWrapText(true);

            btnBuscar.setOnAction(e -> {
                String id = txtId.getText().trim();
                if (id.isEmpty()) {
                    areaResultado.setText("Por favor ingrese un número de identificación");
                    return;
                }

                areaResultado.setText("Consulta de notas para estudiante: " + id + "\n\n" +
                        "Esta funcionalidad está en desarrollo.\n" +
                        "Por ahora, puedes usar la versión de consola.");
            });

            hbox.getChildren().addAll(lbl, txtId, btnBuscar);

            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            btnCerrar.setOnAction(e -> stage.close());

            root.getChildren().addAll(titulo, hbox, areaResultado, btnCerrar);

            stage.setScene(new Scene(root, 600, 500));
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al crear ventana simple: " + e.getMessage());
        }
    }

    @FXML
    public void mostrarExtra() {
        try {
            System.out.println("=== INICIANDO MOSTRAR EXTRA ===");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ExtraView.fxml"));
            Parent root = loader.load();

            ExtraController controller = loader.getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Planes Premium");
            stage.setResizable(true);

            System.out.println("Ventana creada, cargando imagen...");

            String rutaImagen = "C:/Users/USUARIO/OneDrive/Documents/Proyecto-Gestion-Estudiantes/SistemaEstudiantes/resources/images/planes.jpeg";
            System.out.println("Ruta de la imagen: " + rutaImagen);

            File archivoImagen = new File(rutaImagen);
            if (archivoImagen.exists()) {
                System.out.println("✓ Archivo encontrado: " + archivoImagen.length() + " bytes");
                String urlArchivo = "file:" + rutaImagen;
                System.out.println("URL del archivo: " + urlArchivo);
                controller.cargarImagen(urlArchivo);
            } else {
                System.out.println("✗ Archivo NO encontrado en: " + rutaImagen);
                System.out.println("Usando imagen de prueba online...");
                controller.cargarImagen("https://via.placeholder.com/600x400/1D1D47/73FFFF?text=Planes+Premium");
            }

            stage.show();
            System.out.println("✓ Ventana mostrada exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar ExtraView: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar la ventana de planes");
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void cerrarAplicacion() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}