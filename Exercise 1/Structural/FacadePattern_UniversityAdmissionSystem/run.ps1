# University Admission System - Facade Pattern Demonstration
# PowerShell Script for Windows

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host " University Admission System" -ForegroundColor Yellow
Write-Host " Facade Pattern Demonstration" -ForegroundColor Yellow
Write-Host "=====================================" -ForegroundColor Cyan

# Check if compiled classes exist
if (-not (Test-Path "bin\Main.class")) {
    Write-Host "Classes not found! Compiling first..." -ForegroundColor Yellow
    
    # Compile using PowerShell
    Write-Host "Cleaning previous compilation..." -ForegroundColor Gray
    Remove-Item -Path "*.class" -Recurse -ErrorAction SilentlyContinue
    
    # Create output directory
    if (-not (Test-Path "bin")) {
        New-Item -ItemType Directory -Path "bin" | Out-Null
    }
    
    Write-Host "Compiling Java source files..." -ForegroundColor Gray
    
    # Get all Java files
    $sourceFiles = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
    
    # Compile all Java files
    $compileCommand = "javac -d bin -sourcepath src " + ($sourceFiles -join " ")
    Invoke-Expression $compileCommand
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "" 
        Write-Host "========================================" -ForegroundColor Red
        Write-Host " COMPILATION FAILED!" -ForegroundColor Red
        Write-Host " Please check the error messages above" -ForegroundColor Red
        Write-Host "========================================" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host " COMPILATION SUCCESSFUL!" -ForegroundColor Green
    Write-Host " All classes compiled successfully" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
}

Write-Host "Starting University Admission System..." -ForegroundColor Green
Write-Host ""

# Run the application
java -cp bin Main

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host " Application Terminated" -ForegroundColor Yellow
Write-Host " Thank you for using our system!" -ForegroundColor Yellow
Write-Host "=====================================" -ForegroundColor Cyan
Read-Host "Press Enter to exit"
