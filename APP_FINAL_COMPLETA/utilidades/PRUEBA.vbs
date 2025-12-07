Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

currentDir = fso.GetParentFolderName(WScript.ScriptFullName)

MsgBox "PRUEBA DE INSTALACION" & vbCrLf & vbCrLf & _
       "Carpeta actual: " & currentDir & vbCrLf & _
       "Para instalar: " & vbCrLf & _
       "1. Cliquea derecho en INICIAR.vbs" & vbCrLf & _
       "2. 'Enviar a' -> 'Escritorio'" & vbCrLf & _
       "3. Renombra a 'Sistema Estudiantes'", vbInformation, "Instrucciones"
