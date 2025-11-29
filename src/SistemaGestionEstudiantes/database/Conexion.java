package SistemaGestionEstudiantes.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL = "jdbc:sqlite:database/sistema_estudiantes.db";

    public static Connection conectar() {
        try {

            // --- Cargar el driver JDBC ---
            Class.forName("org.sqlite.JDBC");

            // --- Intentar conexión ---
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Conexión exitosa a SQLite.");
            return conn;

        } catch (Exception e) {
            System.out.println("Error al conectar con SQLite: " + e.getMessage());
            return null;
        }
    }
}
