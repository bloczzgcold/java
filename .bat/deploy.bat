@echo off

cd ..
call mvn clean deploy -Dmaven.test.skip=true

pause