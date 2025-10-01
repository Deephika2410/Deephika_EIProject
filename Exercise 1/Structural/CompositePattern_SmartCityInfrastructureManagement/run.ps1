# Smart City Infrastructure Management System
# PowerShell execution script demonstrating Composite Design Pattern

Write-Host "================================================================================" -ForegroundColor Green
Write-Host "         SMART CITY INFRASTRUCTURE MANAGEMENT SYSTEM - EXECUTION" -ForegroundColor Green
Write-Host "                        Composite Design Pattern Demo" -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Green

# Check if bin directory exists
if (-not (Test-Path "bin")) {
    Write-Host ""
    Write-Host "ERROR: 'bin' directory not found!" -ForegroundColor Red
    Write-Host "Please compile the project first by running: .\compile.bat" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Main.class exists
if (-not (Test-Path "bin\Main.class")) {
    Write-Host ""
    Write-Host "ERROR: Main.class not found in 'bin' directory!" -ForegroundColor Red
    Write-Host "Please compile the project first by running: .\compile.bat" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Starting Smart City Infrastructure Management System..." -ForegroundColor Cyan
Write-Host ""

# Display features
Write-Host "Features:" -ForegroundColor Yellow
Write-Host "  - Dynamic city hierarchy creation at runtime" -ForegroundColor White
Write-Host "  - Composite pattern implementation with uniform component treatment" -ForegroundColor White
Write-Host "  - SOLID principles and defensive programming practices" -ForegroundColor White
Write-Host "  - Comprehensive logging and validation" -ForegroundColor White
Write-Host "  - Interactive console interface with 18 menu options" -ForegroundColor White
Write-Host ""

Write-Host "Hierarchy Levels:" -ForegroundColor Yellow
Write-Host "  City -> Districts -> Zones -> Buildings -> Floors -> Devices" -ForegroundColor White
Write-Host ""

Write-Host "Available Device Types:" -ForegroundColor Yellow
Write-Host "  - Smart Lights (LED, Fluorescent, Incandescent)" -ForegroundColor White
Write-Host "  - Air Conditioners (Central, Window, Split)" -ForegroundColor White
Write-Host "  - Sensors (Temperature, Motion, Smoke)" -ForegroundColor White
Write-Host ""

Write-Host "================================================================================" -ForegroundColor Green
Write-Host "                             LAUNCHING APPLICATION..." -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Green

# Run the application
try {
    & java -cp bin Main
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Write-Host ""
        Write-Host "================================================================================" -ForegroundColor Green
        Write-Host "                       APPLICATION TERMINATED SUCCESSFULLY" -ForegroundColor Green
        Write-Host "================================================================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Thank you for using the Smart City Infrastructure Management System!" -ForegroundColor Cyan
        Write-Host "This demonstration showcased:" -ForegroundColor Yellow
        Write-Host "  - Composite Design Pattern implementation" -ForegroundColor White
        Write-Host "  - SOLID principles adherence" -ForegroundColor White
        Write-Host "  - Dynamic runtime hierarchy creation" -ForegroundColor White
        Write-Host "  - Comprehensive error handling and validation" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "================================================================================" -ForegroundColor Red
        Write-Host "                          APPLICATION TERMINATED WITH ERROR" -ForegroundColor Red
        Write-Host "================================================================================" -ForegroundColor Red
        Write-Host ""
        Write-Host "Error Code: $exitCode" -ForegroundColor Red
        Write-Host "Please check the error messages above for troubleshooting information." -ForegroundColor Yellow
        Write-Host ""
    }
} catch {
    Write-Host ""
    Write-Host "================================================================================" -ForegroundColor Red
    Write-Host "                               EXECUTION FAILED" -ForegroundColor Red
    Write-Host "================================================================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Please ensure Java is properly installed and accessible from PATH." -ForegroundColor Yellow
    Write-Host ""
}

Write-Host ""
Read-Host "Press Enter to exit"
