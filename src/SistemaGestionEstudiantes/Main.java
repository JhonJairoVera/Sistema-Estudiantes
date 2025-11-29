package SistemaGestionEstudiantes;

import java.util.Scanner;

/*
 * Clase Main
 * Controla todo el menú principal, menú profesor y menú estudiante.
 */
public class Main {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);

        GestorEstudiante gestorEstudiantes = new GestorEstudiante();
        GestorMaterias gestorMaterias = new GestorMaterias();

        int opcionPrincipal;

        do {
            System.out.println("\n===== SISTEMA DE GESTIÓN ACADÉMICA =====");
            System.out.println("Seleccione su rol:");
            System.out.println("1. Profesor");
            System.out.println("2. Estudiante");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            opcionPrincipal = leer.nextInt();
            leer.nextLine();

            switch (opcionPrincipal) {

                case 1:
                    menuProfesor(leer, gestorEstudiantes, gestorMaterias);
                    break;

                case 2:
                    menuEstudiante(leer, gestorEstudiantes);
                    break;

                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcionPrincipal != 3);

        leer.close();
    }

    // =======================================================================================
    // MENÚ PROFESOR
    // =======================================================================================

    public static void menuProfesor(
            Scanner leer,
            GestorEstudiante gestorEstudiantes,
            GestorMaterias gestorMaterias
    ) {
        int opcion;

        do {
            System.out.println("\n===== MENÚ PROFESOR =====");
            System.out.println("1. Agregar estudiante");
            System.out.println("2. Consultar estudiantes");
            System.out.println("3. Agregar nota a estudiante");
            System.out.println("4. Eliminar estudiante");
            System.out.println("5. Agregar materia nueva");
            System.out.println("6. Volver");
            System.out.print("Opción: ");

            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {

                // --------------------------- AGREGAR ESTUDIANTE ---------------------------
                case 1:

                    System.out.println("\n--- DESEA AGREGAR ESTUDIANTE ---");
                    System.out.println("0. NO Volver");
                    System.out.println("1. SI ");
                    System.out.print("Opción: ");
                    int sub1 = leer.nextInt(); leer.nextLine();
                    if (sub1 == 0) break;

                    System.out.print("Ingrese número de identificación (CC): ");
                    String identificacion = leer.nextLine();

                    System.out.print("Ingrese nombre completo del estudiante: ");
                    String nombre = leer.nextLine();

                    String idGenerado = gestorEstudiantes.agregarEstudiante(identificacion, nombre);

                    System.out.println("\nEstudiante agregado correctamente.");
                    System.out.println("ID interno generado: " + idGenerado);
                    break;

                // --------------------------- CONSULTAR ESTUDIANTES ---------------------------
                case 2:
                    System.out.println("\n===== LISTA DE ESTUDIANTES =====");

                    if (gestorEstudiantes.getEstudiantes().isEmpty()) {
                        System.out.println("No hay estudiantes registrados.");
                    } else {
                        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                            System.out.println(e.toString());

                            e.getNotasPorMateria().forEach((materia, notas) -> {
                                System.out.println("  - " + materia + ": " + notas);
                            });

                            System.out.println("----------------------------------");
                        }
                    }
                    break;

                // --------------------------- AGREGAR NOTA ---------------------------
                case 3:

                    System.out.println("\n===== ESTUDIANTES REGISTRADOS =====");
                    for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                        System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre());
                    }
                    System.out.println("----------------------------------");

                    System.out.print("Ingrese ID interno del estudiante: ");
                    String idCal = leer.nextLine();

                    Estudiante est = gestorEstudiantes.buscarPorId(idCal);

                    if (est == null) {
                        System.out.println(" No existe un estudiante con ese ID.");
                        break;
                    }

                    System.out.println("\nEstudiante encontrado:");
                    System.out.println("ID: " + est.getId());
                    System.out.println("Nombre: " + est.getNombre());
                    System.out.println("CC: " + est.getIdentificacion());
                    System.out.println("\n--- DESEA AGREGAR NOTA ---");
                    System.out.println("0. NO VOLVER ");
                    System.out.println("1. SI CONTINUAR ");
                    System.out.print("Opción: ");
                    int sub3 = leer.nextInt(); leer.nextLine();
                    if (sub3 == 0) break;

                    System.out.println("\nMaterias disponibles:");
                    int i = 1;
                    for (Materia m : gestorMaterias.getMaterias()) {
                        System.out.println(i + ". " + m.getNombre());
                        i++;
                    }

                    System.out.print("Seleccione la materia: ");
                    int opcionMateria = leer.nextInt();
                    leer.nextLine();

                    String materiaSeleccionada =
                            gestorMaterias.getMaterias().get(opcionMateria - 1).getNombre();

                    System.out.print("Ingrese nota (0 a 5): ");
                    double nota = leer.nextDouble();
                    leer.nextLine();

                    if (nota < 0 || nota > 5) {
                        System.out.println("La nota debe estar entre 0 y 5.");
                        break;
                    }

                    gestorEstudiantes.agregarNota(idCal, materiaSeleccionada, nota);
                    System.out.println("Nota registrada correctamente.");
                    break;

                // --------------------------- ELIMINAR ESTUDIANTE ---------------------------
                case 4:



                    System.out.println("\n===== ESTUDIANTES REGISTRADOS =====");
                    for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
                        System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre());
                    }
                    System.out.println("----------------------------------");
                    System.out.println("\n--- SEGURO DE ELIMINAR ESTUDIANTE ---");
                    System.out.println("0. NO Volver");
                    System.out.println("1. SI Continuar");
                    System.out.print("Opción: ");
                    int sub4 = leer.nextInt(); leer.nextLine();
                    if (sub4 == 0) break;
                    System.out.print("Ingrese ID del estudiante a eliminar: ");
                    String idEliminar = leer.nextLine();

                    if (gestorEstudiantes.eliminarEstudiante(idEliminar)) {
                        System.out.println("Estudiante eliminado correctamente.");
                    } else {
                        System.out.println("No existe un estudiante con ese ID.");
                    }
                    break;

                // --------------------------- AGREGAR MATERIA ---------------------------
                case 5:

                    System.out.println("\n--- DESEA AGREGAR NUEVA  MATERIA ---");
                    System.out.println("0. NO Volver");
                    System.out.println("1. SI  Continuar");
                    System.out.print("Opción: ");
                    int sub5 = leer.nextInt(); leer.nextLine();
                    if (sub5 == 0) break;

                    System.out.print("Ingrese nombre de la nueva materia: ");
                    String nuevaMateria = leer.nextLine();

                    if (gestorMaterias.agregarMateria(nuevaMateria)) {
                        System.out.println("Materia agregada exitosamente.");
                    } else {
                        System.out.println("La materia ya existe.");
                    }
                    break;

                case 6:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 6);
    }

    // =======================================================================================
    // MENÚ ESTUDIANTE
    // =======================================================================================

    public static void menuEstudiante(
            Scanner leer,
            GestorEstudiante gestorEstudiantes
    ) {

        System.out.print("Ingrese su número de identificación (CC): ");
        String cc = leer.nextLine();

        Estudiante encontrado = null;

        for (Estudiante e : gestorEstudiantes.getEstudiantes()) {
            if (e.getIdentificacion().equals(cc)) {
                encontrado = e;
                break;
            }
        }

        if (encontrado == null) {
            System.out.println(" No existe estudiante con ese número de identificación.");
            return;
        }

        System.out.println("\n===== BIENVENIDO " + encontrado.getNombre().toUpperCase() + " =====");
        System.out.println("ID interno: " + encontrado.getId());
        System.out.println("Notas por materia:");

        encontrado.getNotasPorMateria().forEach((materia, notas) -> {
            System.out.println(" - " + materia + ": " + notas);
        });

        System.out.println("\nPromedio general: " + encontrado.promedioGeneral());
    }
}
