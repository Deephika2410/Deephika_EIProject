@echo off
echo Running Astronaut Daily Schedule Organizer...
echo.

REM Check if compiled classes exist
if not exist "bin\AstronautScheduleApp.class" (
    echo Application not compiled. Running compilation first...
    call compile.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Compilation failed. Cannot run application.
        pause
        exit /b 1
    )
)

REM Run the application
echo Starting application...
echo.
java -cp bin AstronautScheduleApp

echo.
echo Application terminated.
pause
