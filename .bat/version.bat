@echo off

cd ..

set /p version=please input new version��
echo.

call mvn versions:set -DnewVersion=%version%

pause