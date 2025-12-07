Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

dirActual = fso.GetParentFolderName(WScript.ScriptFullName)

' Mostrar informaci?n
info = "PRUEBA DEL SISTEMA" & vbCrLf & vbCrLf & _
       "Carpeta: " & dirActual & vbCrLf & _
       "Archivos encontrados:" & vbCrLf

' Contar archivos
If fso.FileExists(dirActual & "\SistemaEstudiantes.jar") Then
    info = info & "? SistemaEstudiantes.jar" & vbCrLf
Else
    info = info & "? FALTA SistemaEstudiantes.jar" & vbCrLf
End If

If fso.FileExists(dirActual & "\INICIAR2.vbs") Then
    info = info & "? INICIAR2.vbs" & vbCrLf
Else
    info = info & "? FALTA INICIAR2.vbs" & vbCrLf
End If

If fso.FolderExists(dirActual & "\lib") Then
    info = info & "? Carpeta lib/" & vbCrLf
Else
    info = info & "? FALTA carpeta lib/" & vbCrLf
End If

info = info & vbCrLf & "?Ejecutar aplicaci?n?"
respuesta = MsgBox(info, vbYesNo + vbInformation, "Prueba Sistema")

If respuesta = vbYes Then
    ' Ejecutar INICIAR2.vbs
    If fso.FileExists(dirActual & "\INICIAR2.vbs") Then
        WshShell.Run """" & dirActual & "\INICIAR2.vbs""", 0, False
    Else
        MsgBox "No se encuentra INICIAR2.vbs", vbCritical, "Error"
    End If
End If
