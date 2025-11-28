package SistemaGestionEstudiantes;

import java.util.ArrayList;

/*
 * Clase: GestorEstudiante
 * Maneja:
 * - Creación de estudiantes
 * - Agregar notas por materia
 * - Eliminación
 * - Consulta
 */
public class GestorEstudiante {

    private ArrayList<Estudiante> estudiantes;
    private int contadorId;

    public GestorEstudiante() {
        estudiantes = new ArrayList<>();
        contadorId = 1;
    }

    /*
     * Agrega estudiante con ID autoincremental
     */
    public String agregarEstudiante(String identificacion, String nombre) {
        String id = String.valueOf(contadorId);
        contadorId++;

        estudiantes.add(new Estudiante(id, identificacion, nombre));

        return id;
    }

    /*
     * Busca estudiante por ID
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
     * Agrega nota en una materia a un estudiante
     */
    public boolean agregarNota(String id, String materia, double nota) {
        Estudiante e = buscarPorId(id);
        if (e != null) {
            e.agregarNota(materia, nota);
            return true;
        }
        return false;
    }

    /*
     * Elimina estudiante
     */
    public boolean eliminarEstudiante(String id) {
        Estudiante e = buscarPorId(id);
        if (e != null) {
            estudiantes.remove(e);
            return true;
        }
        return false;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}
