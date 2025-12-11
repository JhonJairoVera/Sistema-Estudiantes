@echo off
chcp 65001 > nul
title Verificador Java - Sistema Estudiantes
color 0B

echo ╔══════════════════════════════════════════╗
echo ║    VERIFICADOR DE JAVA - SISTEMA         ║
echo ╚══════════════════════════════════════════╝
echo.

echo [1] Verificando si tienes Java instalado...
echo.

set JAVA_OK=0
set JAVA_VERSION=

REM Verificar Java en PATH
where java >nul 2>&1
if %errorlevel% equ 0 (
    for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
        set JAVA_VERSION=%%i
    )
    set JAVA_OK=1
    echo [✓] Java encontrado en el sistema
    echo     Version: !JAVA_VERSION!
) else (
    echo [✗] Java NO encontrado en PATH
    echo.
    echo Buscando en otras ubicaciones...
    
    set FOUND=0
    for %%J in (
        "%ProgramFiles%\Java\jre*\bin\java.exe"
        "%ProgramFiles%\Java\jdk*\bin\java.exe"
        "%ProgramFiles(x86)%\Java\jre*\bin\java.exe"
        "%ProgramFiles(x86)%\Java\jdk*\bin\java.exe"
    ) do (
        if exist "%%~J" (
            set JAVA_OK=1
            set FOUND=1
            echo [✓] Java encontrado: %%~J
            goto :mostrar_opciones
        )
    )
    
    if !FOUND! equ 0 (
        echo [✗] Java no encontrado en ninguna ubicacion
    )
)

:mostrar_opciones
echo.
echo ═══════════════════════════════════════════
echo.

if %JAVA_OK% equ 0 (
    echo ⚠ ATENCION: JAVA NO ESTA INSTALADO
    echo.
    echo Para instalar Java:
    echo 1. Abre tu navegador web
    echo 2. Ve a: https://adoptium.net/temurin/releases/
    echo 3. Descarga "Temurin 11" (JDK 11 LTS)
    echo 4. Ejecuta el instalador
    echo 5. IMPORTANTE: Marca "Add to PATH"
    echo 6. Reinicia la computadora
    echo.
    echo Luego vuelve a ejecutar este verificador.
) else (
    echo [✓] Todo listo para ejecutar el sistema
    echo.
    echo Para probar el sistema:
    echo 1. Cierra esta ventana
    echo 2. Ejecuta "EJECUTAR.bat" para probar
    echo 3. O usa "INICIAR.vbs" para uso normal
)

echo.
echo Presiona una tecla para probar Java...
pause >nul
echo.
echo Probando Java:
java -version
echo.
echo Si ves la version de Java arriba, todo esta bien!
echo.
pause