Set WshShell = CreateObject("WScript.Shell")
dirActual = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)

' Ejecutar EJECUTAR.bat directamente
WshShell.Run "cmd /c """ & dirActual & "\EJECUTAR.bat""", 0, False
