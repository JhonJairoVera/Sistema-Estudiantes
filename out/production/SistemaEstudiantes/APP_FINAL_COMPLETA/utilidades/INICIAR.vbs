Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

currentDir = fso.GetParentFolderName(WScript.ScriptFullName)

' Configurar rutas
javaPath = "C:\Program Files\Java\jdk-24\bin\java.exe"
jarFile = currentDir & "\SistemaEstudiantes.jar"
libPath = currentDir & "\lib"

' Verificar Java
If Not fso.FileExists(javaPath) Then
    respuesta = MsgBox("Java 24 no encontrado." & vbCrLf & vbCrLf & _
                      "Esta aplicacion necesita Java 24 para funcionar." & vbCrLf & vbCrLf & _
                      "Quieres abrir la pagina de descarga de Java?" & vbCrLf & _
                      "(Se abrira en tu navegador)", vbYesNo + vbInformation, "Java Requerido")
    
    If respuesta = vbYes Then
        WshShell.Run "https://www.oracle.com/java/technologies/downloads/#jdk24-windows"
    End If
    WScript.Quit 1
End If

' Verificar archivo JAR
If Not fso.FileExists(jarFile) Then
    MsgBox "No se puede encontrar el archivo principal." & vbCrLf & _
           "Asegurate de que SistemaEstudiantes.jar este en:" & vbCrLf & _
           currentDir, vbExclamation, "Archivo no encontrado"
    WScript.Quit 1
End If

' Verificar carpeta lib
If Not fso.FolderExists(libPath) Then
    MsgBox "Faltan librerias necesarias." & vbCrLf & _
           "La carpeta lib\ no se encuentra." & vbCrLf & _
           "Por favor, copia toda la carpeta completa.", vbExclamation, "Librerias faltantes"
    WScript.Quit 1
End If

' Crear carpeta database si no existe
databasePath = currentDir & "\database"
If Not fso.FolderExists(databasePath) Then
    fso.CreateFolder(databasePath)
End If

' Construir comando Java
command = Chr(34) & javaPath & Chr(34) & _
          " --module-path " & Chr(34) & libPath & Chr(34) & _
          " --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base" & _
          " --enable-native-access=ALL-UNNAMED" & _
          " -jar " & Chr(34) & jarFile & Chr(34)

' Ejecutar sin mostrar ventana
WshShell.Run command, 0, False
