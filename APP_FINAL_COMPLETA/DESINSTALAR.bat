@echo off
chcp 65001 > nul
cls
echo.
echo ========================================
echo    DESINSTALAR - Sistema de Estudiantes
echo ========================================
echo.
echo Eliminando acceso directo del escritorio...
echo.

set "SHORTCUT=%USERPROFILE%\Desktop\Sistema Estudiantes.lnk"

if exist "%SHORTCUT%" (
    del "%SHORTCUT%"
    echo ? Acceso directo eliminado
    echo.
    echo El acceso "Sistema Estudiantes" ha sido
    echo removido de tu escritorio.
) else (
    echo ?? No se encontr? el acceso directo
    echo.
    echo El acceso "Sistema Estudiantes" no estaba
    echo en tu escritorio.
)

echo.
echo ?? NOTA: La carpeta de la aplicaci?n NO se eliminar?.
echo       Puedes eliminarla manualmente si lo deseas.
echo.
echo ========================================
echo Presiona cualquier tecla para salir...
pause > nul
