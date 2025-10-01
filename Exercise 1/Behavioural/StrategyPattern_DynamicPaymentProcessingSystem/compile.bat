@echo off
echo Compiling Strategy Pattern Demo...
cd src
javac *.java context/*.java models/*.java strategies/*.java
if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo To run: java Main
) else (
    echo Compilation failed!
)
pause
