@echo off
echo =====================================
echo  Compiling University Admission System
echo  Facade Pattern Demonstration
echo =====================================

REM Clean previous compilation
echo Cleaning previous compilation...
del /Q /S *.class 2>nul

REM Create output directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile Java files with proper classpath
echo Compiling Java source files...
javac -d bin -sourcepath src src\Main.java src\models\*.java src\subsystems\*.java src\facade\*.java src\ui\*.java

if errorlevel 1 (
    echo.
    echo ========================================
    echo  COMPILATION FAILED!
    echo  Please check the error messages above
    echo ========================================
    pause
    exit /b 1
)

echo.
echo =====================================
echo  COMPILATION SUCCESSFUL!
echo  All classes compiled successfully
echo =====================================
echo.
echo To run the application, use: run.bat
echo.
pause
