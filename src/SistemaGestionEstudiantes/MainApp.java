package SistemaGestionEstudiantes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MenuPrincipal.fxml"));
        Scene scene = new Scene(root);

        // Aquí cargas el CSS para el fondo
        scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());

        stage.setTitle("Sistema de Gestión de Estudiantes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
