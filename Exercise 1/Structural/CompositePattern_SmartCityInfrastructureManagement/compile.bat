@echo off
REM Compilation script for Smart City Infrastructure Management System
REM Demonstrates Composite Design Pattern with SOLID principles

echo ================================================================================
echo          SMART CITY INFRASTRUCTURE MANAGEMENT SYSTEM - COMPILATION
echo                         Composite Design Pattern Demo
echo ================================================================================

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

echo.
echo Compiling Smart City Infrastructure Management System...
echo.

REM Compile all Java source files
javac -d bin -cp src src\Main.java src\composite\*.java src\components\*.java src\devices\*.java src\exceptions\*.java src\management\*.java src\ui\*.java src\utils\*.java

REM Check compilation result and display appropriate message
if errorlevel 1 goto compilation_failed

:compilation_success
echo.
echo ================================================================================
echo                            COMPILATION SUCCESSFUL!
echo ================================================================================
echo.
echo All Java source files have been compiled successfully.
echo Compiled classes are located in the 'bin' directory.
echo.
echo To run the application, execute: run.bat
echo.
echo Project Structure:
echo   - Main.java                    : Application entry point
echo   - composite\                   : Composite pattern interfaces and base classes
echo   - components\                  : Concrete composite components (City, District, etc.)
echo   - devices\                     : Leaf components (SmartLight, AC, Sensor)
echo   - exceptions\                  : Custom exception classes
echo   - management\                  : Infrastructure management classes
echo   - ui\                          : User interface classes
echo   - utils\                       : Utility classes (Logger, Validation)
echo.
echo Design Patterns Implemented:
echo   - Composite Pattern (Primary)
echo   - Singleton Pattern (Logger)
echo   - Facade Pattern (SmartCityManager)
echo   - Template Method Pattern (AbstractComposite, AbstractDevice)
echo.
echo SOLID Principles Demonstrated:
echo   - Single Responsibility Principle
echo   - Open/Closed Principle
echo   - Liskov Substitution Principle
echo   - Interface Segregation Principle
echo   - Dependency Inversion Principle
echo.
echo ================================================================================
goto end

:compilation_failed
echo.
echo ================================================================================
echo                              COMPILATION FAILED!
echo ================================================================================
echo.
echo Please check the error messages above and fix any compilation issues.
echo Common issues:
echo   - Missing Java installation or JAVA_HOME not set
echo   - Syntax errors in source files
echo   - Missing dependencies
echo.
echo ================================================================================
goto end

:end
echo.
pause
