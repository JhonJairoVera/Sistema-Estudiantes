package SistemaGestionEstudiantes;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/*
 * Clase Estudiante
 * Representa un estudiante con:
 * - ID (SQLite autoincremental)
 * - Número de identificación (CC)
 * - Nombre
 *
 * NOTA:
 * Ya NO guarda notas ni materias en memoria.
 * Las notas serán consultadas desde la base de datos mediante GestorEstudiante.
 */
public class Estudiante {

    private int id;              // ID de base de datos
    private String identificacion;
    private String nombre;

    public Estudiante(int id, String identificacion, String nombre) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | CC: " + identificacion +
                " | Nombre: " + nombre;
    }
}
