@echo off
chcp 65001 > nul
cd /d "%~dp0"
title Sistema de Estudiantes
color 0A

echo ================================
echo   SISTEMA DE ESTUDIANTES
echo ================================
echo.

echo [1] Buscando Java...

REM Verificar Java de forma SIMPLE
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java no encontrado
    echo.
    echo Instala Java desde: https://adoptium.net
    echo.
    pause
    exit /b 1
)

echo [OK] Java encontrado
java -version

echo.
echo [2] Verificando archivos...
if not exist "SistemaEstudiantes.jar" (
    echo [ERROR] No hay SistemaEstudiantes.jar
    pause
    exit /b 1
)

echo [OK] Archivos listos

echo.
echo [3] Configurando entorno...
if exist "dll\" set "PATH=%~dp0dll;%PATH%"

echo.
echo [4] Ejecutando sistema...
echo ================================

REM === ESTA ES LA PARTE IMPORTANTE ===
REM Comando EXACTO para TU JAR
java --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics --enable-native-access=ALL-UNNAMED -jar SistemaEstudiantes.jar

REM Si falla, probar alternativa
if errorlevel 1 (
    echo.
    echo [INTENTO 2] Sin native access...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar SistemaEstudiantes.jar
)

if errorlevel 1 (
    echo.
    echo [ERROR] No se pudo ejecutar
    echo.
    echo Prueba manualmente:
    echo java -jar SistemaEstudiantes.jar
    echo.
    pause
    exit /b 1
)

echo.
echo [OK] Sistema cerrado
pause
exit /b 0