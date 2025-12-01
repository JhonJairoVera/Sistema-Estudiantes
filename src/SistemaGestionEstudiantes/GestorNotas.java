package SistemaGestionEstudiantes;

import SistemaGestionEstudiantes.database.Conexion;
import SistemaGestionEstudiantes.database.Nota;

import java.sql.*;
import java.util.ArrayList;

public class GestorNotas {

    public boolean eliminarNota(int idNota) {
        String sql = "DELETE FROM notas WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idNota);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error eliminando nota: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Nota> obtenerNotas() {
        ArrayList<Nota> lista = new ArrayList<>();
        String sql = "SELECT * FROM notas";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Nota(
                        rs.getInt("id"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_materia"),
                        rs.getDouble("nota")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo notas: " + e.getMessage());
        }

        return lista;
    }
}
