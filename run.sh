#!/bin/bash
# Clean run script - bypasses IDE DevTools injection
echo "🧹 Cleaning build..."
./gradlew clean --quiet

echo "🔨 Building project..."
./gradlew build --quiet

echo "🚀 Starting application..."
./gradlew bootRun --no-daemon