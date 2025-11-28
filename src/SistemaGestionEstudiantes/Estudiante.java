package SistemaGestionEstudiantes;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Clase: Estudiante
 * Representa a un estudiante con:
 * - ID autoincremental
 * - Número de identificación (CC)
 * - Nombre
 * - Notas por materia (HashMap de ArrayList)
 */
public class Estudiante {

    private String id;
    private String identificacion;
    private String nombre;

    // Cada materia tiene una lista de calificaciones
    private HashMap<String, ArrayList<Double>> notasPorMateria;

    /*
     * Constructor del estudiante
     */
    public Estudiante(String id, String identificacion, String nombre) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.notasPorMateria = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    /*
     * Agrega una materia al estudiante (solo si aún no la tiene)
     */
    public void agregarMateria(String materia) {
        notasPorMateria.putIfAbsent(materia, new ArrayList<>());
    }

    /*
     * Agrega una nota a la materia seleccionada.
     */
    public void agregarNota(String materia, double nota) {
        agregarMateria(materia); // asegura que exista
        notasPorMateria.get(materia).add(nota);
    }

    /*
     * Calcula el promedio total del estudiante (promedio entre todas las materias)
     */
    public double promedioGeneral() {
        double suma = 0;
        int cantidad = 0;

        for (String materia : notasPorMateria.keySet()) {
            for (Double nota : notasPorMateria.get(materia)) {
                suma += nota;
                cantidad++;
            }
        }

        return cantidad == 0 ? 0 : suma / cantidad;
    }

    /*
     * Muestra todo el detalle del estudiante
     */
    public String toString() {
        return "ID: " + id +
                " | CC: " + identificacion +
                " | Nombre: " + nombre +
                " | Promedio general: " + promedioGeneral();
    }

    /*
     * Obtiene todas las notas del estudiante
     */
    public HashMap<String, ArrayList<Double>> getNotasPorMateria() {
        return notasPorMateria;
    }
}
