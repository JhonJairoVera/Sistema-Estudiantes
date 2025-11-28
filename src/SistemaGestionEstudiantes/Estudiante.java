package SistemaGestionEstudiantes;
import java.util.ArrayList;
/*
 * Clase: Estudiante
 * Representa a un estudiante con ID, nombre y calificaciones.
 */
public class Estudiante {
    private String id;                                      // Identificador del estudiante
    private String nombre;                                  // Nombre del estudiante
    private ArrayList<Double> listaCalificaciones;          // Lista de calificaciones

    /*
     * Constructor que inicializa ID, nombre y lista vacía de calificaciones
     */
    public Estudiante(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.listaCalificaciones = new ArrayList<>();
    }

    /*
     * Devuelve el ID del estudiante
     */
    public String getId() {
        return id;
    }

    /*
     * Devuelve el nombre del estudiante
     */
    public String getNombre() {
        return nombre;
    }

    /*
     * Agrega una calificación a la lista del estudiante
     */
    public void agregarCalificacion(double calificacion) {
        listaCalificaciones.add(calificacion);
    }

    /*
     * Calcula y retorna el promedio de las calificaciones.
     * Si no hay calificaciones, devuelve 0.
     */
    public double calcularPromedio() {
        if (listaCalificaciones.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Double c : listaCalificaciones) {
            suma += c;
        }
        return suma / listaCalificaciones.size();
    }

    /*
     * Devuelve la cantidad de calificaciones registradas
     */
    public int cantidadCalificaciones() {
        return listaCalificaciones.size();
    }

    /*
     * Devuelve una representación en texto del estudiante
     * (Sin @Override porque el profesor no lo permite)
     */
    public String toString() {
        return "ID: " + id +
                " | Nombre: " + nombre +
                " | Calificaciones registradas: " + cantidadCalificaciones() +
                " | Promedio: " + calcularPromedio();
    }
}
