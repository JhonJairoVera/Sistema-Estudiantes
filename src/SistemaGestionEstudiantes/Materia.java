package SistemaGestionEstudiantes;

/*
 * Clase Materia
 * Representa una materia con un nombre.
 */
public class Materia {

    private String nombre;

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
