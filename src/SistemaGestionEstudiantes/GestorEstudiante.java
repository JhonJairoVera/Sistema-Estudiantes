package SistemaGestionEstudiantes;
import java.util.ArrayList;
/*
 * Clase: GestorEstudiantes
 * Administra la lista de estudiantes del sistema.
 */
public class GestorEstudiante {
    private ArrayList<Estudiante> estudiantes; // Lista de estudiantes registrados

    /*
     * Inicializa la lista vacía al crear el gestor
     */
    public GestorEstudiante() {
        estudiantes = new ArrayList<>();
    }

    /*
     * Agrega un nuevo estudiante si el ID no está repetido
     */
    public boolean agregarEstudiante(String id, String nombre) {
        if (buscarPorId(id) != null) {
            return false; // Ya existe
        }
        estudiantes.add(new Estudiante(id, nombre));
        return true;
    }

    /*
     * Busca un estudiante por su ID
     */
    public Estudiante buscarPorId(String id) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    /*
     * Agrega una calificación a un estudiante existente
     */
    public boolean agregarCalificacionAEstudiante(String id, double calificacion) {
        Estudiante e = buscarPorId(id);
        if (e != null) {
            e.agregarCalificacion(calificacion);
            return true;
        }
        return false;
    }

    /*
     * Retorna la lista completa de estudiantes
     */
    public ArrayList<Estudiante> consultarEstudiantes() {
        return estudiantes;
    }

    /*
     * Elimina un estudiante si existe
     */
    public boolean eliminarEstudiante(String id) {
        Estudiante e = buscarPorId(id);
        if (e != null) {
            estudiantes.remove(e);
            return true;
        }
        return false;
    }
}
