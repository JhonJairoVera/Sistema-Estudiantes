package SistemaGestionEstudiantes;

import SistemaGestionEstudiantes.database.Conexion;

import java.sql.*;
import java.util.ArrayList;

public class GestorMaterias {

    // ==========================================================
    // OBTENER TODAS LAS MATERIAS DESDE SQLite
    // ==========================================================
    public ArrayList<Materia> getMaterias() {
        ArrayList<Materia> lista = new ArrayList<>();
        String sql = "SELECT * FROM materias";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Materia(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error al obtener materias: " + e.getMessage());
        }

        return lista;
    }

    // ==========================================================
    // AGREGAR NUEVA MATERIA
    // ==========================================================
    public boolean agregarMateria(String nombre) {
        String sql = "INSERT INTO materias(nombre) VALUES(?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("La materia ya existe en la base de datos.");
            } else {
                System.out.println("Error al agregar materia: " + e.getMessage());
            }
            return false;
        }
    }

    // ==========================================================
    // AGREGAR MATERIAS FIJAS (solo si no existen)
    // ==========================================================
    public void crearMateriasFijas() {

        String[] materias = {
                "Matemáticas",
                "Programación",
                "Inglés",
                "Base de Datos"
        };

        for (String m : materias) {
            agregarMateria(m);
        }
    }
}
