package SistemaGestionEstudiantes;

import SistemaGestionEstudiantes.database.DatabaseSetup;
import SistemaGestionEstudiantes.database.GestorEstudiante;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Crear las tablas
        DatabaseSetup.crearTablas();

        Scanner leer = new Scanner(System.in);

        GestorEstudiante gestorEstudiantes = new GestorEstudiante();
        GestorMaterias gestorMaterias = new GestorMaterias();

        int opcionPrincipal;

        do {
            System.out.println("1. Profesor");
            System.out.println("2. Estudiante");
            System.out.println("3. Opción extra");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            opcionPrincipal = leer.nextInt();
            leer.nextLine();

            switch (opcionPrincipal) {
                case 1:

                    System.out.print("Ingrese la contraseña (4 dígitos): ");
                    String pass = leer.nextLine();

                    if (GestorContrasena.verificarContraseña(pass)) {
                        menuProfesor(leer, gestorEstudiantes, gestorMaterias);
                    } else {
                        System.out.println("Contraseña incorrecta.");
                    }
                    break;
                case 2:
                    menuEstudiante(leer, gestorEstudiantes);
                    break;
                case 3:
                    System.out.println("\n===== PLAN PLUS =====");
                    System.out.println("**********************");
                    System.out.println("*   PLAN PLUS BASIC  *");
                    System.out.println("*  - Acceso ilimitado*");
                    System.out.println("*  - Soporte 24/7    *");
                    System.out.println("*  - Actualizaciones *");
                    System.out.println("**********************\n");

                    System.out.println("Presiona ENTER para volver al menú principal...");
                    leer.nextLine();  // Pausa para esperar que el usuario presione ENTER
                    break;

                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcionPrincipal != 4);

        leer.close();
    }

    public static void menuProfesor(Scanner leer, GestorEstudiante gestorEstudiantes, GestorMaterias gestorMaterias) {
        int opcion;

        do {
            System.out.println("\n===== MENÚ PROFESOR =====");
            System.out.println("1. Agregar estudiante");
            System.out.println("2. Consultar estudiantes");
            System.out.println("3. Agregar nota a estudiante");
            System.out.println("4. Eliminar estudiante");
            System.out.println("5. Agregar materia nueva");
            System.out.println("6. Eliminar Materia ");
            System.out.println("7. Modificar contraseña ");
            System.out.println("8. Volver");
            System.out.print("Opción: ");

            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- DESEA AGREGAR ESTUDIANTE ? ---");
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

                    if (idGenerado != null) {
                        System.out.println("Estudiante agregado con ID: " + idGenerado);
                    } else {
                        System.out.println("Error agregando estudiante.");
                    }
                    break;

                case 2:
                    List<Estudiante> estudiantes = gestorEstudiantes.getEstudiantes();
                    if (estudiantes.isEmpty()) {
                        System.out.println("No hay estudiantes registrados.");
                    } else {
                        for (Estudiante e : estudiantes) {
                            System.out.println(e.toString());

                            Map<String, List<Double>> notas = gestorEstudiantes.getNotasPorMateria(e.getId());
                            notas.forEach((materia, listaNotas) -> {
                                System.out.println("  - " + materia + ": " + listaNotas);
                            });

                            double promedio = gestorEstudiantes.promedioGeneral(e.getId());
                            System.out.printf("Promedio general: %.2f\n", promedio);

                            System.out.println("----------------------------------");
                        }
                    }
                    break;

                case 3:
                    List<Estudiante> listaEstudiantes = gestorEstudiantes.getEstudiantes();
                    if (listaEstudiantes.isEmpty()) {
                        System.out.println("No hay estudiantes registrados.");
                        break;
                    }

                    System.out.println("Estudiantes:");
                    for (Estudiante e : listaEstudiantes) {
                        System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre());
                    }
                    System.out.println("\n--- DESEA AGREGAR NOTA ? ---");
                    System.out.println("0. NO VOLVER ");
                    System.out.println("1. SI  ");
                    System.out.print("Opción: ");
                    int sub3 = leer.nextInt(); leer.nextLine();
                    if (sub3 == 0) break;
                    System.out.print("Ingrese ID interno del estudiante: ");
                    String idCal = leer.nextLine();

                    Estudiante est = gestorEstudiantes.buscarPorId(idCal);
                    if (est == null) {
                        System.out.println("No existe un estudiante con ese ID.");
                        break;
                    }

                    System.out.println("\nMaterias disponibles:");
                    List<Materia> materias = gestorMaterias.getMaterias();
                    for (int i = 0; i < materias.size(); i++) {
                        System.out.println((i + 1) + ". " + materias.get(i).getNombre());
                    }
                    System.out.print("Seleccione la materia: ");
                    int opcionMateria = leer.nextInt();
                    leer.nextLine();

                    if (opcionMateria < 1 || opcionMateria > materias.size()) {
                        System.out.println("Opción de materia inválida.");
                        break;
                    }

                    String materiaSeleccionada = materias.get(opcionMateria - 1).getNombre();

                    System.out.print("Ingrese nota (0 a 5): ");
                    double nota = leer.nextDouble();
                    leer.nextLine();

                    if (nota < 0 || nota > 5) {
                        System.out.println("La nota debe estar entre 0 y 5.");
                        break;
                    }

                    if (gestorEstudiantes.agregarNota(idCal, materiaSeleccionada, nota)) {
                        System.out.println("Nota registrada correctamente.");
                    } else {
                        System.out.println("Error registrando nota.");
                    }
                    break;

                case 4:
                    List<Estudiante> estEliminar = gestorEstudiantes.getEstudiantes();
                    if (estEliminar.isEmpty()) {
                        System.out.println("No hay estudiantes registrados.");
                        break;
                    }

                    System.out.println("Estudiantes:");
                    for (Estudiante e : estEliminar) {
                        System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre());
                    }
                    System.out.println("----------------------------------");
                    System.out.println("\n--- ESTA SEGURO DE ELIMINAR UN  ESTUDIANTE ?---");
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

                case 5:
                    System.out.println("\n--- DESEA AGREGAR MATERIA NUEVA ? ---");
                    System.out.println("0. NO VOLVER ");
                    System.out.println("1. SI  ");
                    System.out.print("Opción: ");
                    int sub5 = leer.nextInt(); leer.nextLine();
                    if (sub5 == 0) break;

                    System.out.print("Ingrese nombre de la nueva materia: ");
                    String nuevaMateria = leer.nextLine();

                    if (gestorMaterias.agregarMateria(nuevaMateria)) {
                        System.out.println("Materia agregada exitosamente.");
                    } else {
                        System.out.println("La materia ya existe o hubo un error.");
                    }
                    break;
                case 6:
                    List<Materia> listaMaterias = gestorMaterias.getMaterias();

                    if (listaMaterias.isEmpty()) {
                        System.out.println("No hay materias registradas.");
                        break;
                    }

                    System.out.println("\n===== MATERIAS DISPONIBLES =====");
                    for (Materia m : listaMaterias) {
                        System.out.println("ID: " + m.getId() + " | Nombre: " + m.getNombre());
                    }

                    System.out.println("\n--- ¿Desea eliminar una materia? ---");
                    System.out.println("0. NO volver");
                    System.out.println("1. SI eliminar");
                    System.out.print("Opción: ");
                    int confirmar = leer.nextInt();
                    leer.nextLine();

                    if (confirmar == 0) break;

                    System.out.print("Ingrese el ID de la materia a eliminar: ");
                    int idEliminarMateria = leer.nextInt();
                    leer.nextLine();

                    if (gestorMaterias.eliminarMateria(idEliminarMateria)) {
                        System.out.println("Materia eliminada correctamente.");
                    } else {
                        System.out.println("No existe una materia con ese ID.");
                    }

                    break;

                case 7:
                    System.out.println("\n--- DESEA CAMBIAR LA CONTRASEÑA  ? ---");
                    System.out.println("0. NO VOLVER ");
                    System.out.println("1. SI  ");
                    System.out.print("Opción: ");
                    int sub6 = leer.nextInt(); leer.nextLine();
                    if (sub6 == 0) break;
                    System.out.print("Ingrese la nueva contraseña de 4 dígitos: ");
                    String nuevaPass = leer.nextLine();

                    if (nuevaPass.length() == 4 && nuevaPass.matches("\\d{4}")) {
                        GestorContrasena.cambiarContraseña(nuevaPass);
                        System.out.println("Contraseña cambiada correctamente.");
                    } else {
                        System.out.println("Contraseña inválida. Debe tener 4 dígitos numéricos.");
                    }
                    break;


                case 8:
                    System.out.println("Volviendo al menú principal...");
                    break;


                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 8);
    }

    public static void menuEstudiante(Scanner leer, GestorEstudiante gestorEstudiantes) {
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
            System.out.println("No existe estudiante con ese número de identificación.");
            return;
        }

        System.out.println("\n===== BIENVENIDO " + encontrado.getNombre().toUpperCase() + " =====");
        System.out.println("ID interno: " + encontrado.getId());
        System.out.println("Notas por materia:");

        Map<String, List<Double>> notas = gestorEstudiantes.getNotasPorMateria(encontrado.getId());
        notas.forEach((materia, listaNotas) -> {
            System.out.println(" - " + materia + ": " + listaNotas);
        });

        System.out.println("\nPromedio general: " + gestorEstudiantes.promedioGeneral(encontrado.getId()));
    }
}
