package SistemaGestionEstudiantes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Estudiante {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty identificacion = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();

    // Constructor vacío
    public Estudiante() {}

    // Constructor con parámetros (para tu GestorEstudiante)
    public Estudiante(int id, String identificacion, String nombre) {
        this.id.set(id);
        this.identificacion.set(identificacion);
        this.nombre.set(nombre);
    }

    // ===== PROPERTIES para JavaFX (ESTOS son los que faltan) =====
    public IntegerProperty idProperty() { return id; }
    public StringProperty identificacionProperty() { return identificacion; }
    public StringProperty nombreProperty() { return nombre; }

    // ===== GETTERS tradicionales =====
    public int getId() { return id.get(); }
    public String getIdentificacion() { return identificacion.get(); }
    public String getNombre() { return nombre.get(); }

    // ===== SETTERS tradicionales =====
    public void setId(int id) { this.id.set(id); }
    public void setIdentificacion(String identificacion) { this.identificacion.set(identificacion); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    // toString para consola
    @Override
    public String toString() {
        return "ID: " + getId() + ", CC: " + getIdentificacion() + ", Nombre: " + getNombre();
    }
}