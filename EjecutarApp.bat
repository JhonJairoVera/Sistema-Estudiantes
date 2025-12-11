@echo off
cd /d "%~dp0"
title Sistema de Gestion de Estudiantes

echo ========================================
echo   SISTEMA DE GESTION DE ESTUDIANTES
echo ========================================
echo Iniciando aplicacion...
echo.

set CLASSPATH=out/production/SistemaEstudiantes;APP_FINAL_COMPLETA/lib/javafx.base.jar;APP_FINAL_COMPLETA/lib/javafx.controls.jar;APP_FINAL_COMPLETA/lib/javafx.fxml.jar;APP_FINAL_COMPLETA/lib/javafx.graphics.jar;lib/sqlite-jdbc-3.45.2.0.jar;lib/slf4j-api-2.0.13.jar;lib/slf4j-simple-2.0.13.jar

java -cp "%CLASSPATH%" SistemaGestionEstudiantes.MainApp

if %errorlevel% equ 0 (
    echo.
    echo Aplicacion cerrada correctamente.
) else (
    echo.
    echo ERROR: La aplicacion fallo con codigo %errorlevel%
)
timeout /t 5
