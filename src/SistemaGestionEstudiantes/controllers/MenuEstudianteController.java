package SistemaGestionEstudiantes;
import SistemaGestionEstudiantes.database.GestorEstudiante;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MenuEstudianteController {

    private GestorEstudiante gestorEstudiantes = new GestorEstudiante();

    @FXML
    public void initialize() {
        mostrarDatosEstudiante();
    }

    private void mostrarDatosEstudiante() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ingreso Estudiante");
        dialog.setHeaderText("Ingrese su número de identificación (CC):");
        Optional<String> ccOpt = dialog.showAndWait();

        if (ccOpt.isPresent()) {
            String cc = ccOpt.get();

            Estudiante encontrado = null;
            for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                if (e.getIdentificacion().equals(cc)) {
                    encontrado = e;
                    break;
                }
            }

            if (encontrado == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No existe estudiante con ese número de identificación.");
                alert.showAndWait();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(encontrado.getNombre()).append("\n");
            sb.append("ID interno: ").append(encontrado.getId()).append("\n");
            sb.append("Notas por materia:\n");

            Map<String, List<Double>> notas = gestorEstudiantes.getNotasPorMateria(encontrado.getId());
            notas.forEach((materia, listaNotas) -> {
                sb.append(" - ").append(materia).append(": ").append(listaNotas).append("\n");
            });

            sb.append("\nPromedio general: ").append(gestorEstudiantes.promedioGeneral(encontrado.getId()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datos Estudiante");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
    }
}
