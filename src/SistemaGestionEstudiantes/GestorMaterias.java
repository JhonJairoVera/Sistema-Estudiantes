package SistemaGestionEstudiantes;

import java.util.ArrayList;

/*
 * Clase GestorMaterias
 * Administra las materias disponibles:
 * - Materias fijas
 * - Materias agregadas por el profesor
 */
public class GestorMaterias {

    private ArrayList<Materia> materias;

    public GestorMaterias() {
        materias = new ArrayList<>();

        // MATERIAS FIJAS
        materias.add(new Materia("Matemáticas"));
        materias.add(new Materia("Programación"));
        materias.add(new Materia("Inglés"));
        materias.add(new Materia("Base de Datos"));
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public boolean agregarMateria(String nombre) {
        // evita duplicados
        for (Materia m : materias) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }
        materias.add(new Materia(nombre));
        return true;
    }
}
