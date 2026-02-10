#!/bin/bash

JAR_FILE="backend/target/pios-backend-1.0.0.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "JAR file not found. Please run build.sh first."
    exit 1
fi

echo "Starting PIOS Backend..."
java -jar "$JAR_FILE" --spring.profiles.active=dev
