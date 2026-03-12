#!/bin/bash
cd /Users/jsimone/galvanize/movieproject
echo "=== Cleaning build ==="
./gradlew clean >/dev/null 2>&1
echo "=== Compiling Java code ==="
./gradlew compileJava -x test 2>&1 | tail -50
echo ""
echo "=== Checking compilation status ==="
if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
else
    echo "✗ Compilation failed"
    exit 1
fi
echo ""
echo "=== Running tests ==="
./gradlew test 2>&1 | tail -100

