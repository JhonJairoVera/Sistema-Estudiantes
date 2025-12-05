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
import javafx.stage.StageStyle;

public class MenuPrincipalController {

    @FXML private Button btnDocente;
    @FXML private Button btnEstudiante;
    @FXML private Button btnExtra;
    @FXML private Button btnCerrar;
    @FXML private Button btnAcercaDe; // Nuevo botón

    @FXML
    public void initialize() {
        System.out.println("MenuPrincipalController inicializado");
    }

    // ==================== MÉTODO PARA "ACERCA DE" ====================
    @FXML
    private void mostrarAcercaDe() {
        try {
            System.out.println("=== ABRIENDO VENTANA 'ACERCA DE' ===");

            // Cargar el archivo FXML de AcercaDe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AcercaDe.fxml"));
            Parent root = loader.load();

            // Crear nueva ventana
            Stage acercaDeStage = new Stage();
            acercaDeStage.setTitle("Acerca de SPES - Sistema de Gestión Estudiantil");
            acercaDeStage.setScene(new Scene(root));
            acercaDeStage.setResizable(false);

            // Centrar en la pantalla
            acercaDeStage.centerOnScreen();

            // Mostrar ventana
            acercaDeStage.show();

            System.out.println("✓ Ventana 'Acerca de' abierta exitosamente");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar AcercaDe.fxml: " + e.getMessage());

            // Mostrar mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar la ventana 'Acerca de'");
            alert.setContentText("Verifica que el archivo AcercaDe.fxml exista en la carpeta views.\nError: " + e.getMessage());
            alert.show();
        } catch (NullPointerException e) {
            System.err.println("Archivo AcercaDe.fxml no encontrado");

            // Crear una ventana simple de emergencia
            crearVentanaAcercaDeEmergencia();
        }
    }

    /**
     * Método de emergencia si no se encuentra el archivo FXML
     */
    private void crearVentanaAcercaDeEmergencia() {
        Stage stage = new Stage();
        stage.setTitle("Acerca de SPES");

        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 30; -fx-background-color: #1D1D47; -fx-alignment: center;");

        Label titulo = new Label("Seguimiento y Progreso Mediante la Evaluación Sistemática");
        titulo.setStyle("-fx-text-fill: #73FFFF; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center;");

        Label descripcion = new Label("SPES: Sistema de Gestión Estudiantil");
        descripcion.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        VBox equipoBox = new VBox(10);
        equipoBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 10;");

        Label equipoTitle = new Label("Equipo de Desarrollo:");
        equipoTitle.setStyle("-fx-text-fill: #73FFFF; -fx-font-weight: bold;");

        Label po = new Label("• Jhon Jairo Vera Acevedo - Product Owner");
        po.setStyle("-fx-text-fill: white;");

        Label sm = new Label("• Nicolás Steven Pineda Rodríguez - Scrum Master");
        sm.setStyle("-fx-text-fill: white;");

        Label dev = new Label("• Deyner Alberto Contreras Sandoval - Developer");
        dev.setStyle("-fx-text-fill: white;");

        Label version = new Label("Versión 2.0 © 2024");
        version.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnCerrar.setOnAction(e -> stage.close());

        equipoBox.getChildren().addAll(equipoTitle, po, sm, dev);
        root.getChildren().addAll(titulo, descripcion, equipoBox, version, btnCerrar);

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    // ==================== MÉTODOS EXISTENTES ====================
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
            // Cargar la vista de consulta de estudiante
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ConsultaEstudianteView.fxml"));
            Parent root = loader.load();

            // Crear Stage
            Stage stage = new Stage();
            stage.setTitle("Consulta de Notas - Estudiante");

            // Crear Scene y asignarla al Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // IMPORTANTE: Permitir redimensionar y mostrar botones de ventana
            stage.setResizable(true); // Permitir maximizar

            // Asegurarse de que la ventana tenga decoraciones de sistema
            stage.initStyle(StageStyle.DECORATED); // Esto es el valor por defecto

            // Centrar en la pantalla
            stage.centerOnScreen();

            // Mostrar ventana
            stage.show();

            System.out.println("✓ Ventana de consulta de estudiante abierta correctamente");

        } catch (Exception e) {
            System.err.println("✗ Error al abrir consulta de estudiante: " + e.getMessage());
            e.printStackTrace();
            mostrarError("No se pudo abrir la ventana de consulta de estudiante.\nError: " + e.getMessage());
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