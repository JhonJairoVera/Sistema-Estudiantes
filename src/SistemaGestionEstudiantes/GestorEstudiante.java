package SistemaGestionEstudiantes.database;

import SistemaGestionEstudiantes.Estudiante;

import java.sql.*;
import java.util.ArrayList;

public class GestorEstudiante {

    // ===============================
    // LISTAR ESTUDIANTES (SELECT *)
    // ===============================
    public ArrayList<Estudiante> getEstudiantes() {
        ArrayList<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT id, cc, nombre FROM estudiantes";

        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cc = rs.getString("cc");
                String nombre = rs.getString("nombre");

                lista.add(new Estudiante(id, cc, nombre));
            }

        } catch (Exception e) {
            System.out.println("Error al obtener estudiantes: " + e.getMessage());
        }

        return lista;
    }

    // ============================================
    // AGREGAR ESTUDIANTE (INSERT INTO)
    // Devuelve el id generado como String o null si falla
    // ============================================
    public String agregarEstudiante(String cc, String nombre) {

        String sql = "INSERT INTO estudiantes (cc, nombre) VALUES (?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cc);
            ps.setString(2, nombre);
            ps.executeUpdate();

            // Obtener ID generado automáticamente
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return String.valueOf(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al agregar estudiante: " + e.getMessage());
        }

        return null;
    }

    // ============================================
    // BUSCAR ESTUDIANTE POR ID
    // ============================================
    public Estudiante buscarPorId(String idStr) {

        String sql = "SELECT id, cc, nombre FROM estudiantes WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int id = Integer.parseInt(idStr);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("cc"),
                            rs.getString("nombre")
                    );
                }
            }

        } catch (NumberFormatException nfe) {
            System.out.println("ID inválido: " + idStr);
        } catch (Exception e) {
            System.out.println("Error al buscar por ID: " + e.getMessage());
        }

        return null;
    }

    // ============================================
    // BUSCAR ESTUDIANTE POR CC
    // ============================================
    public Estudiante buscarPorCC(String cc) {

        String sql = "SELECT id, cc, nombre FROM estudiantes WHERE cc = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("cc"),
                            rs.getString("nombre")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar por CC: " + e.getMessage());
        }

        return null;
    }

    // ============================================
    // ELIMINAR ESTUDIANTE
    // ============================================
    public boolean eliminarEstudiante(String idStr) {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int id = Integer.parseInt(idStr);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (NumberFormatException nfe) {
            System.out.println("ID inválido: " + idStr);
        } catch (Exception e) {
            System.out.println("Error eliminando estudiante: " + e.getMessage());
        }

        return false;
    }

    /*
     * Verifica si ya existe un estudiante con esa CC
     */
    public boolean existeCC(String cc) {
        String sql = "SELECT COUNT(*) AS total FROM estudiantes WHERE cc = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0; // si COUNT > 0 → ya existe
                }
            }

        } catch (Exception e) {
            System.out.println("Error verificando CC: " + e.getMessage());
        }
        return false;
    }

    /*
     * Obtiene un estudiante por su número de identificación (CC)
     */
    public Estudiante obtenerPorCC(String cc) {

        String sql = "SELECT id, cc, nombre FROM estudiantes WHERE cc = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("cc"),
                            rs.getString("nombre")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error buscando estudiante por CC: " + e.getMessage());
        }

        return null;
    }

    /*
     * Actualiza el nombre del estudiante
     */
    public boolean actualizarNombre(String idStr, String nuevoNombre) {

        String sql = "UPDATE estudiantes SET nombre = ? WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int id = Integer.parseInt(idStr);
            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (NumberFormatException nfe) {
            System.out.println("ID inválido: " + idStr);
        } catch (Exception e) {
            System.out.println("Error actualizando estudiante: " + e.getMessage());
        }
        return false;
    }
}
