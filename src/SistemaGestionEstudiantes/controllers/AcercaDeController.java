package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AcercaDeController {

    @FXML
    private Button btnCerrar;

    @FXML
    private void initialize() {
        System.out.println("✅ Controlador AcercaDe inicializado");
    }

    @FXML
    private void cerrarVentana() {
        System.out.println("Cerrando ventana Acerca de...");
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}