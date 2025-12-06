import java.sql.*;

public class VerificarBD {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/USUARIO/SistemaEstudiantesApp/database/sistema_estudiantes.db";
        
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            
            System.out.println("=== ESTRUCTURA DE LA BASE DE DATOS ===");
            
            // 1. Mostrar tablas
            System.out.println("\n📊 TABLAS EXISTENTES:");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
            }
            
            // 2. Mostrar columnas de 'estudiantes'
            System.out.println("\n📋 COLUMNAS DE 'estudiantes':");
            ResultSet columns = meta.getColumns(null, null, "estudiantes", "%");
            while (columns.next()) {
                System.out.println("  - " + columns.getString("COLUMN_NAME") + 
                                 " (" + columns.getString("TYPE_NAME") + ")");
            }
            
            // 3. Mostrar datos
            System.out.println("\n📝 DATOS EN 'estudiantes':");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM estudiantes LIMIT 3");
            
            // Mostrar nombres de columnas
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();
            
            // Mostrar datos
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
