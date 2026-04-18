#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
./gradlew :desktop:dist
echo ""
echo "Build complete. JAR is at:"
echo "  $(pwd)/desktop/build/libs/desktop-1.0.jar"
