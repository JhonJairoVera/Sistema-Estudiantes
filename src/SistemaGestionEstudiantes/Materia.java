package SistemaGestionEstudiantes;

/*
 * Clase Materia
 * Representa una materia con:
 * - ID en base de datos
 * - Nombre
 */
public class Materia {

    private int id;
    private String nombre;

    public Materia(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
