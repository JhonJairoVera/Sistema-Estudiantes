Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

dirActual = fso.GetParentFolderName(WScript.ScriptFullName)

' Mensaje de inicio
WshShell.Popup "Iniciando Sistema de Estudiantes...", 2, "Inicio", 64

' Verificar Java
javaPath = "C:\Program Files\Java\jdk-24\bin\java.exe"
If Not fso.FileExists(javaPath) Then
    MsgBox "ERROR: Java 24 no encontrado." & vbCrLf & vbCrLf & _
           "Descarga Java desde oracle.com/java/", vbCritical, "Error Java"
    WScript.Quit 1
End If

' Verificar archivos locales
If Not fso.FileExists(dirActual & "\SistemaEstudiantes.jar") Then
    MsgBox "ERROR: No se encuentra el archivo principal.", vbCritical, "Error"
    WScript.Quit 1
End If

If Not fso.FolderExists(dirActual & "\lib") Then
    MsgBox "ERROR: No se encuentra carpeta lib.", vbCritical, "Error"
    WScript.Quit 1
End If

' Crear database si no existe
If Not fso.FolderExists(dirActual & "\database") Then
    fso.CreateFolder(dirActual & "\database")
End If

' Ejecutar EJECUTAR.bat (que ya tiene todo configurado)
batFile = dirActual & "\EJECUTAR.bat"
If fso.FileExists(batFile) Then
    ' Usar EJECUTAR.bat si existe
    WshShell.Run "cmd /c """ & batFile & """", 0, False
Else
    ' Si no existe EJECUTAR.bat, ejecutar directo
    cmd = "cmd /c """ & javaPath & """ --module-path """ & dirActual & "\lib"" --add-modules javafx.controls,javafx.fxml -jar """ & dirActual & "\SistemaEstudiantes.jar"""
    WshShell.Run cmd, 0, False
End If
