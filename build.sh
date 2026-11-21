#!/bin/bash
# Compiles all Java files into the "out" folder.
mkdir -p out
javac -d out $(find src test -name "*.java") && echo "Build OK"
