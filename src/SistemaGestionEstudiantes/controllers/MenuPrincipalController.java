package SistemaGestionEstudiantes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MenuPrincipalController {

    @FXML
    private void irMenuProfesor(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acción");
        alert.setHeaderText(null);
        alert.setContentText("Has seleccionado Profesor");
        alert.showAndWait();
    }

    @FXML
    private void irMenuEstudiante(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acción");
        alert.setHeaderText(null);
        alert.setContentText("Has seleccionado Estudiante");
        alert.showAndWait();
    }
}
