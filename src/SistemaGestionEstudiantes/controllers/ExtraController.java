package SistemaGestionEstudiantes.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ExtraController {

    @FXML
    private ImageView imagenPlan;

    @FXML
    private void initialize() {
        System.out.println("ImageView inicializado. Dimensiones:");
        System.out.println("  FitWidth: " + imagenPlan.getFitWidth());
        System.out.println("  FitHeight: " + imagenPlan.getFitHeight());

        // Configurar para mejor calidad
        imagenPlan.setSmooth(true);
        imagenPlan.setCache(true);
        imagenPlan.setPreserveRatio(true);
    }

    /**
     * Método mejorado para cargar imágenes con más información
     */
    public void cargarImagen(String urlImagen) {
        System.out.println("\n=== CARGANDO IMAGEN ===");
        System.out.println("URL proporcionada: " + urlImagen);

        try {
            // Configurar listeners para debugging
            Image imagen = new Image(urlImagen, true); // true = cargar en background

            // Listener para progreso
            imagen.progressProperty().addListener((obs, oldVal, newVal) -> {
                double porcentaje = newVal.doubleValue() * 100;
                System.out.printf("Progreso de carga: %.1f%%\n", porcentaje);

                if (newVal.doubleValue() == 1.0) {
                    System.out.println("✓ Imagen completamente cargada");
                    System.out.println("  Dimensiones: " + imagen.getWidth() + "x" + imagen.getHeight());
                    System.out.println("  PreserveRatio: " + imagenPlan.isPreserveRatio());
                    System.out.println("  Smooth: " + imagenPlan.isSmooth());
                }
            });

            // Listener para errores
            imagen.errorProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    System.err.println("✗ ERROR al cargar la imagen");
                    if (imagen.getException() != null) {
                        System.err.println("  Excepción: " + imagen.getException().getMessage());
                        imagen.getException().printStackTrace();
                    }
                }
            });

            // Asignar la imagen
            imagenPlan.setImage(imagen);

            // Verificar inmediatamente si hay error
            if (imagen.isError()) {
                System.err.println("Error inmediato al crear la imagen");
            } else {
                System.out.println("Imagen asignada al ImageView");
            }

        } catch (Exception e) {
            System.err.println("Excepción al cargar imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana() {
        try {
            Stage stage = (Stage) imagenPlan.getScene().getWindow();
            stage.close();
            System.out.println("Ventana cerrada");
        } catch (Exception e) {
            System.err.println("Error al cerrar ventana: " + e.getMessage());
        }
    }
}