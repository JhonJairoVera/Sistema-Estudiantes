Set objShell = CreateObject("WScript.Shell")
currentDir = Left(WScript.ScriptFullName, InStrRev(WScript.ScriptFullName, "\") - 1)

' Comando para ejecutar Java
javaCmd = """C:\Program Files\Java\jdk-24\bin\java.exe"" --module-path """ & currentDir & "\lib"" --add-modules javafx.controls,javafx.fxml,javafx.graphics --enable-native-access=ALL-UNNAMED -jar """ & currentDir & "\SistemaEstudiantes.jar"""

' Crear archivo BAT temporal
Set fso = CreateObject("Scripting.FileSystemObject")
tempBat = objShell.ExpandEnvironmentStrings("%TEMP%") & "\ejecutar_sin_cmd.bat"

' Escribir BAT que se auto-elimina
Set file = fso.CreateTextFile(tempBat, True)
file.WriteLine "@echo off"
file.WriteLine "chcp 65001 > nul"
file.WriteLine "cd /d """ & currentDir & """"
file.WriteLine "if not exist database mkdir database"
file.WriteLine javaCmd
file.WriteLine "del """ & tempBat & """"
file.Close

' Ejecutar BAT oculto
objShell.Run "cmd /c """ & tempBat & """", 0, False
