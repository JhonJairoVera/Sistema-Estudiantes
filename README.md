Sistema de Gestión de Estudiantes
Descripción del Proyecto
Sistema de gestión académica desarrollado en Java con JavaFX para administrar estudiantes, materias y notas. Incluye interfaz gráfica moderna, base de datos SQLite y sistema de autenticación por contraseña.

Estructura del Proyecto
text
SistemaEstudiantes/
├──  src/
│   └──  SistemaGestionEstudiantes/
│       ├── controllers/           # Controladores de JavaFX
│       │   ├── MenuPrincipalController.java
│       │   ├── MenuProfesorController.java
│       │   ├── MenuEstudianteController.java
│       │   └── MenuAdministradorController.java
│       ├── database/              # Capa de acceso a datos
│       │   ├── Conexion.java         # Gestión de conexión SQLite
│       │   ├── DatabaseSetup.java    # Creación de tablas
│       │   ├── EstudianteDAO.java    # Operaciones CRUD estudiantes
│       │   ├── GestorEstudiante.java # Lógica de negocio estudiantes
│       │   ├── GestorContrasena.java # Gestión de contraseñas
│       │   ├── Estudiante.java       # Modelo de datos
│       │   └── Nota.java             # Modelo de notas
│       └── Main.java                 # Punto de entrada
├──  resources/
│   ├──  views/                     # Archivos FXML
│   │   ├── MenuPrincipal.fxml
│   │   ├── MenuProfesor.fxml
│   │   ├── MenuEstudiante.fxml
│   │   └── MenuAdministrador.fxml
│   ├── css/                       # Estilos CSS
│   └──  images/                    # Imágenes e íconos
├──  lib/                           # Librerías externas
│   ├── sqlite-jdbc.jar              # Driver SQLite
│   └── javafx-sdk-25.0.1/           # JavaFX SDK
├──  database/                      # Base de datos SQLite
│   └── sistema_estudiantes.db       # Archivo de base de datos
├──  documentacion/                 # Documentación del proyecto
├── EjecutarApp.bat                  # Script de ejecución Windows
├── temp_schema.sql                  # Esquema de base de datos
└── README.md                        # Este archivo
 Características Principales
Sistema de Acceso
Dos de usuario: Estudiante, Profesor.

Autenticación por contraseña (actualmente: 1234 para profesor)

Interfaces personalizadas según el rol

 Gestión de Estudiantes
 Agregar nuevos estudiantes

Editar información existente

 Eliminar estudiantes

 Buscar por cc

 Visualizar en tabla interactiva

 Gestión Académica
Registrar materias

 Asignar notas a estudiantes

Calcular promedios


Base de Datos
SQLite como motor de base de datos

Tablas principales:

estudiantes (id, cc, nombre, apellido, etc.)

materias (id, nombre)

notas (id, id_estudiante, id_materia, nota)

Persistencia automática de datos

 Tecnologías Utilizadas
Tecnología	Versión	Propósito
Java	JDK 21+	Lenguaje principal
JavaFX	25.0.1	Interfaz gráfica
SQLite	3.45.1.0	Base de datos embebida
FXML	2.0	Diseño de interfaces
CSS	3.0	Estilos y temas
⚙️ Requisitos del Sistema
Software Requerido
Java Development Kit (JDK) 21 o superior

JavaFX SDK 25.0.1

SQLite JDBC Driver 3.45.1.0

Configuración Recomendada
Windows 10/11, 

4 GB RAM mínimo

500 MB espacio en disco

Pantalla 1366x768 o superior

🚀 Instalación y Ejecución
Método 1: Ejecutar desde IntelliJ IDEA
Clonar o descargar el proyecto

Abrir en IntelliJ IDEA

Configurar SDK (Java 21+)

Agregar librerías en Project Structure:

lib/sqlite-jdbc.jar

lib/javafx-sdk-25.0.1/lib/*

Ejecutar Main.java

Método 2: Script de Ejecución (Windows)
bash
# Ejecutar el archivo batch
EjecutarApp.bat
Método 3: Línea de Comandos
bash
# Compilar
javac -cp "lib/sqlite-jdbc.jar;lib/javafx-sdk-25.0.1/lib/*" src/SistemaGestionEstudiantes/*.java

# Ejecutar
java --module-path "lib/javafx-sdk-25.0.1/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "lib/sqlite-jdbc.jar;src" \
     SistemaGestionEstudiantes.Main
 Guía de Uso
1. Pantalla Principal
Seleccionar rol de usuario

Ingresar contraseña correspondiente

Acceder a la interfaz específica

2. Menú del Profesor
Gestión de Estudiantes: Ver todos los estudiantes registrados

Agregar Estudiante: Formulario para nuevo registro

Buscar: Filtrado por nombre o cédula

Reportes: Visualización de datos académicos

3. Menú del Estudiante
Consultar información personal

Ver notas por materia

Consultar promedio general



Esquema de Base de Datos
Tabla: estudiantes
sql
CREATE TABLE estudiantes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cc TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    edad INTEGER,
    correo TEXT,
    telefono TEXT,
    carrera TEXT,
    semestre INTEGER,
    fecha_ingreso DATE
);
Tabla: materias
sql
CREATE TABLE materias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    creditos INTEGER,
    profesor TEXT
);
Tabla: notas
sql
CREATE TABLE notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_estudiante INTEGER NOT NULL,
    id_materia INTEGER NOT NULL,
    nota REAL NOT NULL,
    periodo TEXT,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id),
    FOREIGN KEY (id_materia) REFERENCES materias(id)
);
🔧 Solución de Problemas
Error: "Driver SQLite no encontrado"
Solución: Verificar que lib/sqlite-jdbc.jar existe y está en el classpath

Error: "Cannot invoke Connection.createStatement()"
Solución: La base de datos no se está creando. Ejecutar DatabaseSetup.crearTablas()

Error: FXML no carga
Solución: Verificar rutas en los controladores y que los archivos .fxml estén en resources/views/

Warnings de JavaFX
Solución: Agregar --enable-native-access=javafx.graphics a las VM Options

 Consideraciones de Seguridad
Notas importantes:

La contraseña actual está codificada en el código (solo para desarrollo)

En producción, implementar sistema de hash y salting

La base de datos SQLite no está encriptada por defecto

Considerar implementar SQLCipher para encriptación

 Mantenimiento y Actualización
Backup de Base de Datos
bash
# Copiar manualmente
copy database\sistema_estudiantes.db backup\
Actualizar Dependencias
JavaFX: Descargar nueva versión de https://gluonhq.com/products/javafx/

SQLite JDBC: https://github.com/xerial/sqlite-jdbc/releases

 Contribución
Estructura de Desarrollo
develop - Rama de desarrollo activo

feature/* - Nuevas funcionalidades

hotfix/* - Correcciones urgentes

Convenciones de Código
Nombres en inglés para clases y métodos

Comentarios en español para documentación

Seguir patrones MVC (Modelo-Vista-Controlador)

 Licencia
© 2025 Sistema de Gestión de Estudiantes. Desarrollado para fines educativos.

 Créditos
Desarrollador Principal: [jhon jairo vera acevedo ]
Asignatura: fundamentos de programacion
Versión: 1.0.0 Final

 Soporte
Para problemas técnicos:

Verificar que todas las librerías estén instaladas

Revisar consola para mensajes de error específicos

Consultar archivo documentacion/ para más detalles
