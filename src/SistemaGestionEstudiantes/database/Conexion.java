package SistemaGestionEstudiantes.database;

import java.sql.*;

public class Conexion {
    private static final String URL = "jdbc:sqlite:database/sistema_estudiantes.db";
    
    public static Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Conexion exitosa a SQLite.");
            
            crearTablasCompatibles(conn);
            
            return conn;
        } catch (Exception e) {
            System.err.println("Error SQLite: " + e.getMessage());
            return null;
        }
    }
    
    private static void crearTablasCompatibles(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            
            // Tabla estudiantes
            stmt.execute("CREATE TABLE IF NOT EXISTS estudiantes (id INTEGER PRIMARY KEY AUTOINCREMENT, cc TEXT NOT NULL, nombre TEXT NOT NULL)");
            
            // Tabla materias
            stmt.execute("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");
            
            // Tabla notas - USANDO LOS NOMBRES CORRECTOS
            stmt.execute("CREATE TABLE IF NOT EXISTS notas (id INTEGER PRIMARY KEY AUTOINCREMENT, id_estudiante INTEGER NOT NULL, id_materia INTEGER NOT NULL, nota REAL NOT NULL)");
            
            System.out.println("Tablas creadas con nombres compatibles");
            
        } catch (Exception e) {
            System.err.println("Error creando tablas: " + e.getMessage());
        }
    }
}
