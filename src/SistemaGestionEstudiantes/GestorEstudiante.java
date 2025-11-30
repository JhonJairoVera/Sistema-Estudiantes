package SistemaGestionEstudiantes.database;

import SistemaGestionEstudiantes.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorEstudiante {

    // Listar estudiantes
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

    // Agregar estudiante
    public String agregarEstudiante(String cc, String nombre) {
        String sql = "INSERT INTO estudiantes (cc, nombre) VALUES (?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cc);
            ps.setString(2, nombre);
            ps.executeUpdate();

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

    // Buscar estudiante por ID
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

    // Eliminar estudiante
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

    // Agregar nota para estudiante y materia
    public boolean agregarNota(String idEstudianteStr, String nombreMateria, double nota) {
        String sqlBuscarMateria = "SELECT id FROM materias WHERE nombre = ?";
        String sqlInsertarNota = "INSERT INTO notas (id_estudiante, id_materia, nota) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement psBuscarMateria = conn.prepareStatement(sqlBuscarMateria);
             PreparedStatement psInsertarNota = conn.prepareStatement(sqlInsertarNota)) {

            int idEstudiante = Integer.parseInt(idEstudianteStr);

            // Buscar id materia
            psBuscarMateria.setString(1, nombreMateria);
            ResultSet rs = psBuscarMateria.executeQuery();
            if (!rs.next()) {
                System.out.println("Materia no encontrada: " + nombreMateria);
                return false;
            }
            int idMateria = rs.getInt("id");

            // Insertar nota
            psInsertarNota.setInt(1, idEstudiante);
            psInsertarNota.setInt(2, idMateria);
            psInsertarNota.setDouble(3, nota);

            int filas = psInsertarNota.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error agregando nota: " + e.getMessage());
            return false;
        }
    }

    // Obtener notas por materia para un estudiante
    public Map<String, List<Double>> getNotasPorMateria(int idEstudiante) {
        String sql = "SELECT m.nombre as materia, n.nota FROM notas n " +
                "JOIN materias m ON n.id_materia = m.id " +
                "WHERE n.id_estudiante = ?";

        Map<String, List<Double>> notasPorMateria = new HashMap<>();

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String materia = rs.getString("materia");
                double nota = rs.getDouble("nota");

                notasPorMateria.computeIfAbsent(materia, k -> new ArrayList<>()).add(nota);
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo notas por materia: " + e.getMessage());
        }

        return notasPorMateria;
    }

    // Calcular promedio general de un estudiante
    public double promedioGeneral(int idEstudiante) {
        String sql = "SELECT AVG(nota) as promedio FROM notas WHERE id_estudiante = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("promedio");
            }

        } catch (Exception e) {
            System.out.println("Error calculando promedio general: " + e.getMessage());
        }
        return 0.0;
    }
}
