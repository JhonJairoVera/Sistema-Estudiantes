package SistemaGestionEstudiantes;
import java.util.Scanner;
/*
 * Clase principal con menú para gestionar estudiantes y calificaciones.
 */
public class Main {
    public static void main(String[] args) {

        Scanner leer  = new Scanner(System.in);
        GestorEstudiante gestor = new GestorEstudiante();
        int opcion;

        do {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE ESTUDIANTES ===");
            System.out.println("1. Agregar estudiante");
            System.out.println("2. Consultar estudiantes y promedios");
            System.out.println("3. Agregar calificación a estudiante");
            System.out.println("4. Eliminar estudiante");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            opcion =leer.nextInt();
            leer.nextLine();

            switch (opcion) {

                case 1:
                    System.out.print("Ingrese ID del estudiante: ");
                    String id = leer.nextLine();

                    System.out.print("Ingrese nombre del estudiante: ");
                    String nombre = leer.nextLine();

                    if (gestor.agregarEstudiante(id, nombre)) {
                        System.out.println("Estudiante agregado correctamente.");
                    } else {
                        System.out.println("Ya existe un estudiante con ese ID.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE ESTUDIANTES ---");

                    if (gestor.consultarEstudiantes().isEmpty()) {
                        System.out.println("No hay estudiantes registrados.");
                    } else {
                        for (Estudiante e : gestor.consultarEstudiantes()) {
                            System.out.println(e.toString());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Ingrese ID del estudiante: ");
                    String idCal = leer.nextLine();

                    System.out.print("Ingrese calificación (0 a 5): ");
                    double nota = leer.nextDouble();

                    if (nota < 0 || nota > 5) {
                        System.out.println("⚠ La calificación debe estar entre 0 y 5.");
                        break;
                    }

                    if (gestor.agregarCalificacionAEstudiante(idCal, nota)) {
                        System.out.println("Calificación agregada.");
                    } else {
                        System.out.println("No existe un estudiante con ese ID.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese ID del estudiante a eliminar: ");
                    String idEliminar = leer.nextLine();

                    if (gestor.eliminarEstudiante(idEliminar)) {
                        System.out.println("Estudiante eliminado.");
                    } else {
                        System.out.println(" No existe un estudiante con ese ID.");
                    }
                    break;

                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("⚠ Opción no válida.");
            }

        } while (opcion != 5);

        leer.close();
    }
}
