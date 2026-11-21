@echo off
REM Compiles all Java files into the "out" folder (Windows).
if not exist out mkdir out
dir /s /b src\*.java test\*.java > sources.txt
javac -d out @sources.txt && echo Build OK
del sources.txt
