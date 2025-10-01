# Medical Patient Record Prototype System - PowerShell Script

Write-Host "Starting Medical Patient Record Prototype System..." -ForegroundColor Green
Write-Host ""

# Check if compiled
if (-not (Test-Path "src\Main.class")) {
    Write-Host "Classes not found. Compiling first..." -ForegroundColor Yellow
    & .\compile.bat
    Write-Host ""
}

# Run the application
if (Test-Path "src\Main.class") {
    Write-Host "Running application..." -ForegroundColor Green
    Write-Host ""
    java -cp src Main
} else {
    Write-Host "Error: Compilation required. Please run compile.bat first." -ForegroundColor Red
}

Write-Host ""
Write-Host "Application terminated." -ForegroundColor Blue
Read-Host "Press Enter to exit"
