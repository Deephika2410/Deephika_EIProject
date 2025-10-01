@echo off
echo Compiling Astronaut Daily Schedule Organizer...
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile all Java files
javac -d bin -cp src src\*.java src\models\*.java src\factories\*.java src\observers\*.java src\services\*.java src\utils\*.java src\analyzers\*.java src\exporters\*.java src\exceptions\*.java src\ui\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo Run the application using: run.bat
) else (
    echo.
    echo Compilation failed! Please check the errors above.
)

echo.
pause
