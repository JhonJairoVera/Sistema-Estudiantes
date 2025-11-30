package SistemaGestionEstudiantes;

import java.io.*;

public class GestorContrasena {
    private static final String RUTA_ARCHIVO = "contraseña.txt"; // Archivo para guardar la contraseña
    private static String contrasena = "1234"; // Valor por defecto

    // Cargar la contraseña desde el archivo al iniciar la clase
    static {
        File archivo = new File(RUTA_ARCHIVO);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea = br.readLine();
                if (linea != null && linea.matches("\\d{4}")) {
                    contrasena = linea;
                }
            } catch (IOException e) {
                System.out.println("Error leyendo archivo de contraseña: " + e.getMessage());
            }
        } else {
            // Si no existe archivo, lo crea con la contraseña por defecto
            guardarContraseñaEnArchivo(contrasena);
        }
    }

    public static boolean verificarContraseña(String entrada) {
        return contrasena.equals(entrada);
    }

    public static void cambiarContraseña(String nuevaContraseña) {
        if (nuevaContraseña != null && nuevaContraseña.length() == 4 && nuevaContraseña.matches("\\d{4}")) {
            contrasena = nuevaContraseña;
            guardarContraseñaEnArchivo(nuevaContraseña);
        }
    }

    private static void guardarContraseñaEnArchivo(String pass) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            bw.write(pass);
        } catch (IOException e) {
            System.out.println("Error guardando contraseña en archivo: " + e.getMessage());
        }
    }

    public static String getContraseña() {
        return contrasena; // solo para pruebas, mejor no dejar en producción
    }
}
