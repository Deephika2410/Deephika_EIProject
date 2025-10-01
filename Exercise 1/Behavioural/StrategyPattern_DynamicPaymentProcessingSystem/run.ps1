# Strategy Pattern - Payment System Runner
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Strategy Pattern - Payment System" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Compiling Java files..." -ForegroundColor Green
Set-Location src

# Compile all Java files
$compileResult = javac *.java context/*.java models/*.java strategies/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Compilation successful! Starting application..." -ForegroundColor Green
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    
    # Run the application
    java Main
} else {
    Write-Host ""
    Write-Host "Compilation failed! Please check for errors." -ForegroundColor Red
    Read-Host "Press Enter to continue..."
}

Write-Host ""
Write-Host "Application finished." -ForegroundColor Yellow
Read-Host "Press Enter to continue..."
