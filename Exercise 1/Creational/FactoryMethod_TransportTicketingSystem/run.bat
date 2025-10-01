@echo off
echo ==========================================
echo   TRANSPORT TICKETING SYSTEM - RUNNING
echo ==========================================

cd src

if not exist "Main.class" (
    echo ERROR: Main.class not found!
    echo Please compile the project first using compile.bat
    cd ..
    pause
    exit /b 1
)

echo Starting the Transport Ticketing System...
echo.

java -cp . Main

cd ..

echo.
echo ==========================================
echo   APPLICATION TERMINATED
echo ==========================================

pause
