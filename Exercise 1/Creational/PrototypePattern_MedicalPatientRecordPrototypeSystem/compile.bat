@echo off
echo Compiling Medical Patient Record Prototype System...
echo.

rem Clean previous compilation
if exist "src\*.class" del /q "src\*.class"
if exist "src\models\*.class" del /q "src\models\*.class"
if exist "src\prototype\*.class" del /q "src\prototype\*.class"
if exist "src\enums\*.class" del /q "src\enums\*.class"
if exist "src\management\*.class" del /q "src\management\*.class"
if exist "src\ui\*.class" del /q "src\ui\*.class"

rem Compile all Java files
javac -cp src -d src src\Main.java src\enums\*.java src\models\*.java src\prototype\*.java src\management\*.java src\ui\*.java

if %errorlevel% == 0 (
    echo.
    echo Compilation successful!
    echo To run the application, use: run.bat
) else (
    echo.
    echo Compilation failed! Please check for errors.
)

pause
