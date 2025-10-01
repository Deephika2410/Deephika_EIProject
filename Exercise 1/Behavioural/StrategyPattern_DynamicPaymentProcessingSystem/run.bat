@echo off
echo ========================================
echo  Strategy Pattern - Payment System
echo ========================================
echo.

echo Compiling Java files...
cd src
javac *.java context/*.java models/*.java strategies/*.java managers/*.java services/*.java ui/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful! Starting application...
    echo.
    echo ========================================
    java -cp . Main
) else (
    echo.
    echo Compilation failed! Please check for errors.
    pause
)

echo.
echo Application finished.
pause
