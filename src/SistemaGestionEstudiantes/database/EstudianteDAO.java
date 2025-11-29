package SistemaGestionEstudiantes.database;

import SistemaGestionEstudiantes.Estudiante;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteDAO {

    // ===================== INSERTAR ESTUDIANTE =====================
    public static int insertar(String cc, String nombre) {
        String sql = "INSERT INTO estudiantes(cc, nombre) VALUES(?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cc);
            stmt.setString(2, nombre);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            System.out.println("Error al insertar estudiante: " + e.getMessage());
        }
        return -1;
    }

    // ===================== OBTENER TODOS =====================
    public static ArrayList<Estudiante> obtenerTodos() {

        ArrayList<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT * FROM estudiantes";

        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante e = new Estudiante(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("cc"),
                        rs.getString("nombre")
                );
                lista.add(e);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener estudiantes: " + e.getMessage());
        }

        return lista;
    }

    // ===================== BUSCAR POR ID =====================
    public static Estudiante buscarPorId(int id) {

        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Estudiante(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("cc"),
                        rs.getString("nombre")
                );
            }

        } catch (Exception e) {
            System.out.println("Error al buscar estudiante: " + e.getMessage());
        }

        return null;
    }

    // ===================== ELIMINAR =====================
    public static boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
        }

        return false;
    }
}
