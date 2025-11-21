@echo off
REM Script para ejecutar tests del proyecto Spring Boot
REM Autor: GitHub Copilot
REM Fecha: 2025-11-21

echo.
echo ========================================
echo   TESTING PYRAMID - Spring Boot
echo ========================================
echo.

:menu
echo Seleccione una opcion:
echo.
echo [1] Ejecutar TODOS los tests
echo [2] Ejecutar solo tests UNITARIOS
echo [3] Ejecutar solo tests de INTEGRACION
echo [4] Ejecutar solo tests E2E
echo [5] Ejecutar tests con reporte detallado
echo [6] Limpiar y ejecutar todos los tests
echo [7] Ver reporte HTML en navegador
echo [0] Salir
echo.
set /p option="Ingrese su opcion: "

if "%option%"=="1" goto all_tests
if "%option%"=="2" goto unit_tests
if "%option%"=="3" goto integration_tests
if "%option%"=="4" goto e2e_tests
if "%option%"=="5" goto detailed_tests
if "%option%"=="6" goto clean_tests
if "%option%"=="7" goto open_report
if "%option%"=="0" goto end

echo Opcion invalida, intente nuevamente.
echo.
goto menu

:all_tests
echo.
echo Ejecutando TODOS los tests...
echo.
gradlew.bat test --console=plain
goto show_result

:unit_tests
echo.
echo Ejecutando tests UNITARIOS (ClienteServiceTest)...
echo.
gradlew.bat test --tests *ClienteServiceTest* --console=plain
goto show_result

:integration_tests
echo.
echo Ejecutando tests de INTEGRACION (ClienteControllerIT)...
echo.
gradlew.bat test --tests *ClienteControllerIT* --console=plain
goto show_result

:e2e_tests
echo.
echo Ejecutando tests E2E (ClienteE2ETest)...
echo.
gradlew.bat test --tests *ClienteE2ETest* --console=plain
goto show_result

:detailed_tests
echo.
echo Ejecutando tests con reporte detallado...
echo.
gradlew.bat test --console=plain --info
goto show_result

:clean_tests
echo.
echo Limpiando proyecto y ejecutando todos los tests...
echo.
gradlew.bat clean test --console=plain
goto show_result

:open_report
echo.
echo Abriendo reporte HTML en navegador...
echo.
if exist "build\reports\tests\test\index.html" (
    start "" "build\reports\tests\test\index.html"
    echo Reporte abierto correctamente.
) else (
    echo ERROR: No se encontro el reporte. Ejecute los tests primero.
)
echo.
pause
goto menu

:show_result
echo.
echo ========================================
echo   RESULTADO DE LA EJECUCION
echo ========================================
echo.
if %ERRORLEVEL% EQU 0 (
    echo [OK] Todos los tests pasaron correctamente! 
    echo.
    echo Desea ver el reporte HTML? [S/N]
    set /p view_report=""
    if /i "%view_report%"=="S" (
        start "" "build\reports\tests\test\index.html"
    )
) else (
    echo [ERROR] Algunos tests fallaron. 
    echo Revise el reporte en: build\reports\tests\test\index.html
    echo.
    pause
)
echo.
goto menu

:end
echo.
echo Saliendo...
echo.
exit /b 0
