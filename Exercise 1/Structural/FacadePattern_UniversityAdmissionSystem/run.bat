@echo off
echo =====================================
echo  University Admission System
echo  Facade Pattern Demonstration
echo =====================================

REM Check if compiled classes exist
if not exist "bin\Main.class" (
    echo Classes not found! Compiling first...
    call compile.bat
    if errorlevel 1 (
        echo Compilation failed! Cannot run the application.
        pause
        exit /b 1
    )
)

echo Starting University Admission System...
echo.

REM Run the application
java -cp bin Main

echo.
echo =====================================
echo  Application Terminated
echo  Thank you for using our system!
echo =====================================
pause
