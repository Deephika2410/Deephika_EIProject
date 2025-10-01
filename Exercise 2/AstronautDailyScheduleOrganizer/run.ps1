# PowerShell script to run Astronaut Daily Schedule Organizer
Write-Host "Running Astronaut Daily Schedule Organizer..." -ForegroundColor Green
Write-Host ""

# Check if compiled classes exist
if (-not (Test-Path "bin\AstronautScheduleApp.class")) {
    Write-Host "Application not compiled. Running compilation first..." -ForegroundColor Yellow
    & .\compile.bat
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Compilation failed. Cannot run application." -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Run the application
Write-Host "Starting application..." -ForegroundColor Green
Write-Host ""
java -cp bin AstronautScheduleApp

Write-Host ""
Write-Host "Application terminated." -ForegroundColor Yellow
Read-Host "Press Enter to exit"
