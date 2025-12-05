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

    // ==========================================================
    // ELIMINAR MATERIA POR ID CON INTEGRIDAD REFERENCIAL
    // ==========================================================
    public boolean eliminarMateria(int id) {
        String sqlEliminarNotas = "DELETE FROM notas WHERE id_materia = ?";
        String sqlEliminarMateria = "DELETE FROM materias WHERE id = ?";

        try (Connection conn = Conexion.conectar()) {
            // Iniciar transacción
            conn.setAutoCommit(false);

            try {
                // 1. Primero eliminar todas las notas de esta materia
                try (PreparedStatement psNotas = conn.prepareStatement(sqlEliminarNotas)) {
                    psNotas.setInt(1, id);
                    int notasEliminadas = psNotas.executeUpdate();
                    System.out.println("Notas eliminadas: " + notasEliminadas);
                }

                // 2. Luego eliminar la materia
                try (PreparedStatement psMateria = conn.prepareStatement(sqlEliminarMateria)) {
                    psMateria.setInt(1, id);
                    int rows = psMateria.executeUpdate();

                    if (rows > 0) {
                        conn.commit(); // Confirmar transacción
                        System.out.println("✅ Materia eliminada con ID: " + id);
                        return true;
                    } else {
                        conn.rollback(); // Revertir si no se eliminó la materia
                        System.out.println("❌ No se encontró materia con ID: " + id);
                        return false;
                    }
                }

            } catch (SQLException e) {
                conn.rollback(); // Revertir en caso de error
                System.out.println("❌ Error al eliminar materia: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error de conexión al eliminar materia: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // CONTAR NOTAS POR MATERIA (para mostrar advertencia)
    // ==========================================================
    public int contarNotasPorMateria(int idMateria) {
        String sql = "SELECT COUNT(*) as total FROM notas WHERE id_materia = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Error contando notas por materia: " + e.getMessage());
        }
        return 0;
    }
}