package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import SistemaGestionEstudiantes.GestorContrasena;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML private Button btnDocente;
    @FXML private Button btnEstudiante;
    @FXML private Button btnExtra;
    @FXML private Button btnCerrar;

    // Añade esto para la contraseña
    @FXML private PasswordField passwordField;

    private GestorContrasena gestorContrasena = new GestorContrasena();

    @FXML
    public void initialize() {
        // Los eventos ya están en el FXML, pero puedes mantenerlo así
    }

    @FXML
    private void abrirMenuProfesor() {
        // Necesitamos pedir la contraseña primero
        Stage stage = (Stage) btnDocente.getScene().getWindow();

        // Crear un diálogo para la contraseña
        PasswordDialog dialog = new PasswordDialog();
        dialog.showAndWait().ifPresent(password -> {
            if (GestorContrasena.verificarContraseña(password)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SistemaGestionEstudiantes/views/MenuProfesor.fxml"));
                    Parent root = loader.load();

                    Stage profesorStage = new Stage();
                    profesorStage.setTitle("Menú del Profesor");
                    profesorStage.setScene(new Scene(root, 900, 700));
                    profesorStage.show();

                    // Cerrar la ventana actual
                    stage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    mostrarError("Error al cargar el menú del profesor");
                }
            } else {
                mostrarError("Contraseña incorrecta. Intente nuevamente.");
            }
        });
    }

    @FXML
    private void abrirMenuEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SistemaGestionEstudiantes/views/MenuEstudiante.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Acceso Estudiante");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

            // Cerrar ventana actual
            ((Stage) btnEstudiante.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar el menú del estudiante");
        }
    }

    @FXML
    private void mostrarExtra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SistemaGestionEstudiantes/views/ExtraView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Plan Plus");
            stage.setScene(new Scene(root));

            // Añadir ícono de cerrar (X)
            stage.setOnCloseRequest(e -> {
                System.out.println("Ventana Extra cerrada");
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    // Clase interna para el diálogo de contraseña
    private static class PasswordDialog extends javafx.scene.control.Dialog<String> {
        public PasswordDialog() {
            setTitle("Acceso Profesor");
            setHeaderText("Ingrese la contraseña de 4 dígitos");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Contraseña");

            javafx.scene.control.ButtonType loginButtonType = new javafx.scene.control.ButtonType("Aceptar", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(loginButtonType, javafx.scene.control.ButtonType.CANCEL);

            getDialogPane().setContent(passwordField);

            setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return passwordField.getText();
                }
                return null;
            });
        }
    }
}