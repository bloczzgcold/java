@echo off

cd ..
call mvn clean install -Dmaven.test.skip=true


call mvn clean

pause