package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExtraController {

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        // Inicialización si es necesaria
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}