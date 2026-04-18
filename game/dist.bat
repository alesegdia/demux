@echo off
cd /d "%~dp0"
call gradlew.bat :desktop:dist
echo.
echo Build complete. JAR is at:
echo   %~dp0desktop\build\libs\desktop-1.0.jar
