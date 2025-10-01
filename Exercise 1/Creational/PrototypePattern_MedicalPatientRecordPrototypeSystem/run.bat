@echo off
echo Starting Medical Patient Record Prototype System...
echo.

rem Check if compiled
if not exist "src\Main.class" (
    echo Classes not found. Compiling first...
    call compile.bat
    echo.
)

rem Run the application
if exist "src\Main.class" (
    echo Running application...
    echo.
    java -cp src Main
) else (
    echo Error: Compilation required. Please run compile.bat first.
)

echo.
echo Application terminated.
pause
