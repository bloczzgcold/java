@echo off

cd ..
call mvn clean -Dmaven.test.skip=true

pause