Set WshShell = CreateObject("WScript.Shell")
currentDir = Left(WScript.ScriptFullName, InStrRev(WScript.ScriptFullName, "\") - 1)

' Configurar DLLs si existen
If CreateObject("Scripting.FileSystemObject").FolderExists(currentDir & "\dll") Then
    Set procEnv = WshShell.Environment("PROCESS")
    procEnv("PATH") = currentDir & "\dll;" & procEnv("PATH")
End If

' Crear comando IDÉNTICO a tu .bat
Dim cmd
cmd = "cmd /c chcp 65001 > nul && " & _
      "cd /d """ & currentDir & """ && " & _
      "java --module-path ""lib"" --add-modules javafx.controls,javafx.fxml,javafx.graphics --enable-native-access=ALL-UNNAMED -jar SistemaEstudiantes.jar"

' Ejecutar OCULTO (0) y ESPERAR (True)
WshShell.Run cmd, 0, True

' Verificar si falló
If Err.Number <> 0 Then
    ' Intentar sin native-access
    cmd = "cmd /c chcp 65001 > nul && " & _
          "cd /d """ & currentDir & """ && " & _
          "java --module-path ""lib"" --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar SistemaEstudiantes.jar"
    WshShell.Run cmd, 0, True
End If

Set WshShell = Nothing