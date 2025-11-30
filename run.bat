@echo off
echo Starting DocumentFlow Application...
echo.

cd /d "%~dp0"

REM Clean and compile the project
call mvn clean compile

REM Run the application using JavaFX plugin
call mvn javafx:run

pause
