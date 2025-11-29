package SistemaGestionEstudiantes.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void crearTablas() {

        String sqlEstudiantes =
                "CREATE TABLE IF NOT EXISTS estudiantes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cc TEXT NOT NULL," +
                        "nombre TEXT NOT NULL" +
                        ");";

        String sqlMaterias =
                "CREATE TABLE IF NOT EXISTS materias (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT UNIQUE NOT NULL" +
                        ");";

        String sqlNotas =
                "CREATE TABLE IF NOT EXISTS notas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id_estudiante INTEGER," +
                        "id_materia INTEGER," +
                        "nota REAL," +
                        "FOREIGN KEY(id_estudiante) REFERENCES estudiantes(id)," +
                        "FOREIGN KEY(id_materia) REFERENCES materias(id)" +
                        ");";

        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlEstudiantes);
            stmt.execute(sqlMaterias);
            stmt.execute(sqlNotas);

            System.out.println("Tablas creadas correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }
}
