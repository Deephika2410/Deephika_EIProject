@echo off
REM Execution script for Smart City Infrastructure Management System
REM Demonstrates Composite Design Pattern with SOLID principles

echo ================================================================================
echo          SMART CITY INFRASTRUCTURE MANAGEMENT SYSTEM - EXECUTION
echo                         Composite Design Pattern Demo
echo ================================================================================

REM Check if bin directory exists
if not exist "bin" (
    echo.
    echo ERROR: 'bin' directory not found!
    echo Please compile the project first by running: compile.bat
    echo.
    pause
    exit /b 1
)

REM Check if Main.class exists
if not exist "bin\Main.class" (
    echo.
    echo ERROR: Main.class not found in 'bin' directory!
    echo Please compile the project first by running: compile.bat
    echo.
    pause
    exit /b 1
)

echo.
echo Starting Smart City Infrastructure Management System...
echo.
echo Features:
echo   - Dynamic city hierarchy creation at runtime
echo   - Composite pattern implementation with uniform component treatment
echo   - SOLID principles and defensive programming practices
echo   - Comprehensive logging and validation
echo   - Interactive console interface with 18 menu options
echo.
echo Hierarchy Levels:
echo   City -> Districts -> Zones -> Buildings -> Floors -> Devices
echo.
echo Available Device Types:
echo   - Smart Lights (LED, Fluorescent, Incandescent)
echo   - Air Conditioners (Central, Window, Split)
echo   - Sensors (Temperature, Motion, Smoke)
echo.
echo ================================================================================
echo                              LAUNCHING APPLICATION...
echo ================================================================================

REM Run the application
java -cp bin Main

REM Check exit code
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================================================
    echo                        APPLICATION TERMINATED SUCCESSFULLY
    echo ================================================================================
    echo.
    echo Thank you for using the Smart City Infrastructure Management System!
    echo This demonstration showcased:
    echo   - Composite Design Pattern implementation
    echo   - SOLID principles adherence
    echo   - Dynamic runtime hierarchy creation
    echo   - Comprehensive error handling and validation
    echo.
) else (
    echo.
    echo ================================================================================
    echo                           APPLICATION TERMINATED WITH ERROR
    echo ================================================================================
    echo.
    echo Error Code: %ERRORLEVEL%
    echo Please check the error messages above for troubleshooting information.
    echo.
)

echo.
pause
