#!/bin/bash

echo "=== Advent of Code 2025 - Day 4 ==="
echo ""

cd "$(dirname "$0")"

echo "Kompiluji projekt..."
./gradlew build --quiet

if [ $? -eq 0 ]; then
    echo "✓ Kompilace úspěšná"
    echo ""
    echo "Spouštím řešení..."
    echo ""
    java -cp build/classes/kotlin/main Day04Kt
else
    echo "✗ Chyba při kompilaci"
    exit 1
fi
