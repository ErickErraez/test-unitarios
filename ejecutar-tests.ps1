# Script PowerShell para ejecutar tests del proyecto Spring Boot
# Autor: GitHub Copilot
# Fecha: 2025-11-21

function Show-Menu {
    Clear-Host
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   TESTING PYRAMID - Spring Boot" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Seleccione una opción:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "[1] Ejecutar TODOS los tests" -ForegroundColor White
    Write-Host "[2] Ejecutar solo tests UNITARIOS" -ForegroundColor Green
    Write-Host "[3] Ejecutar solo tests de INTEGRACIÓN" -ForegroundColor Yellow
    Write-Host "[4] Ejecutar solo tests E2E" -ForegroundColor Magenta
    Write-Host "[5] Ejecutar tests con reporte detallado" -ForegroundColor White
    Write-Host "[6] Limpiar y ejecutar todos los tests" -ForegroundColor Cyan
    Write-Host "[7] Ver reporte HTML en navegador" -ForegroundColor Blue
    Write-Host "[8] Mostrar resumen de tests" -ForegroundColor DarkGray
    Write-Host "[0] Salir" -ForegroundColor Red
    Write-Host ""
}

function Show-TestSummary {
    Write-Host ""
    Write-Host "=== RESUMEN DE TESTS ===" -ForegroundColor Green
    Write-Host ""
    
    $serviceTests = (Select-String -Path "src\test\java\com\example\demo\service\ClienteServiceTest.java" -Pattern "@Test").Count
    $controllerTests = (Select-String -Path "src\test\java\com\example\demo\controller\ClienteControllerIT.java" -Pattern "@Test").Count
    $e2eTests = (Select-String -Path "src\test\java\com\example\demo\e2e\ClienteE2ETest.java" -Pattern "@Test").Count
    
    Write-Host "🧪 Tests Unitarios (ClienteServiceTest): " -NoNewline -ForegroundColor Cyan
    Write-Host "$serviceTests" -ForegroundColor White
    
    Write-Host "🌐 Tests de Integración (ClienteControllerIT): " -NoNewline -ForegroundColor Yellow
    Write-Host "$controllerTests" -ForegroundColor White
    
    Write-Host "🚀 Tests E2E (ClienteE2ETest): " -NoNewline -ForegroundColor Magenta
    Write-Host "$e2eTests" -ForegroundColor White
    
    Write-Host ""
    Write-Host "TOTAL DE TESTS: " -NoNewline -ForegroundColor Green
    Write-Host "$($serviceTests + $controllerTests + $e2eTests)" -ForegroundColor White
    Write-Host ""
}

function Execute-Tests {
    param (
        [string]$Command,
        [string]$Description
    )
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host $Description -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    
    $result = Invoke-Expression $Command
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   RESULTADO DE LA EJECUCIÓN" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Todos los tests pasaron correctamente!" -ForegroundColor Green
        Write-Host ""
        $viewReport = Read-Host "¿Desea ver el reporte HTML? (S/N)"
        if ($viewReport -eq "S" -or $viewReport -eq "s") {
            Open-Report
        }
    } else {
        Write-Host "❌ Algunos tests fallaron." -ForegroundColor Red
        Write-Host "Revise el reporte en: build\reports\tests\test\index.html" -ForegroundColor Yellow
        Write-Host ""
    }
    
    Write-Host ""
    Write-Host "Presione cualquier tecla para continuar..." -ForegroundColor DarkGray
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}

function Open-Report {
    $reportPath = "build\reports\tests\test\index.html"
    if (Test-Path $reportPath) {
        Start-Process $reportPath
        Write-Host "✅ Reporte abierto correctamente." -ForegroundColor Green
    } else {
        Write-Host "❌ ERROR: No se encontró el reporte. Ejecute los tests primero." -ForegroundColor Red
    }
}

# Bucle principal
do {
    Show-Menu
    $option = Read-Host "Ingrese su opción"
    
    switch ($option) {
        "1" {
            Execute-Tests ".\gradlew.bat test --console=plain" "Ejecutando TODOS los tests..."
        }
        "2" {
            Execute-Tests ".\gradlew.bat test --tests *ClienteServiceTest* --console=plain" "Ejecutando tests UNITARIOS (ClienteServiceTest)..."
        }
        "3" {
            Execute-Tests ".\gradlew.bat test --tests *ClienteControllerIT* --console=plain" "Ejecutando tests de INTEGRACIÓN (ClienteControllerIT)..."
        }
        "4" {
            Execute-Tests ".\gradlew.bat test --tests *ClienteE2ETest* --console=plain" "Ejecutando tests E2E (ClienteE2ETest)..."
        }
        "5" {
            Execute-Tests ".\gradlew.bat test --console=plain --info" "Ejecutando tests con reporte detallado..."
        }
        "6" {
            Execute-Tests ".\gradlew.bat clean test --console=plain" "Limpiando proyecto y ejecutando todos los tests..."
        }
        "7" {
            Write-Host ""
            Write-Host "Abriendo reporte HTML en navegador..." -ForegroundColor Cyan
            Write-Host ""
            Open-Report
            Write-Host ""
            Write-Host "Presione cualquier tecla para continuar..." -ForegroundColor DarkGray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "8" {
            Show-TestSummary
            Write-Host "Presione cualquier tecla para continuar..." -ForegroundColor DarkGray
            $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        }
        "0" {
            Write-Host ""
            Write-Host "👋 Saliendo... ¡Hasta pronto!" -ForegroundColor Cyan
            Write-Host ""
            break
        }
        default {
            Write-Host ""
            Write-Host "❌ Opción inválida, intente nuevamente." -ForegroundColor Red
            Write-Host ""
            Start-Sleep -Seconds 2
        }
    }
} while ($option -ne "0")
