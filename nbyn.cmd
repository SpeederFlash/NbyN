@echo off
javac -d jnbyn jnbyn\*.java
if [%1] == [] goto end
:file
java -cp jnbyn tech.sfqr.nbyn.NbyN jnbyn\tests\%~n1%~x1
goto end
:end
set /p=Hit ENTER to continue and remove compiled artifacts:
rmdir /Q /S jlox\lox\com
