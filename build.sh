#!/bin/bash

set -e

echo "Building PIOS Project..."

cd backend
mvn clean package -DskipTests
cd ..

cd frontend
npm install
npm run build
cd ..

echo "Build completed!"
echo "Backend JAR: backend/target/pios-backend-1.0.0.jar"
echo "Frontend dist: frontend/dist/"
