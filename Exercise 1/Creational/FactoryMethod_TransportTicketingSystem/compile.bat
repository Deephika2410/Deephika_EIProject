@echo off
echo ==========================================
echo    COMPILING TRANSPORT TICKETING SYSTEM
echo ==========================================

cd src

echo Compiling enums...
javac enums\*.java

echo Compiling models...
javac models\*.java

echo Compiling management classes...
javac -cp . management\*.java

echo Compiling ticket classes...
javac -cp . ticket\*.java

echo Compiling factory classes...
javac -cp . factory\*.java

echo Compiling system classes...
javac -cp . system\*.java

echo Compiling input handler...
javac -cp . input\*.java

echo Compiling management classes...
javac -cp . management\*.java

echo Compiling main class...
javac -cp . Main.java

cd ..

if exist "src\Main.class" (
    echo.
    echo ==========================================
    echo    COMPILATION SUCCESSFUL!
    echo ==========================================
    echo You can now run the application using run.bat
) else (
    echo.
    echo ==========================================
    echo    COMPILATION FAILED!
    echo ==========================================
    echo Please check for errors and try again.
)

pause
