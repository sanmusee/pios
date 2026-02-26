#!/bin/bash

echo "=== MySQL Post-Install Configuration ==="

echo "Waiting for MySQL installation..."
while ! which mysql > /dev/null 2>&1; do
    sleep 5
done

echo "MySQL installed successfully!"
mysql --version

echo ""
echo "Setting up MySQL auto-start..."
brew services start mysql

sleep 3

echo ""
echo "MySQL service status:"
brew services list | grep mysql

echo ""
echo "Initializing database..."
mysql -u root -e "CREATE DATABASE IF NOT EXISTS pios_macro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || echo "Database may already exist or root password is set"

echo ""
echo "=== Configuration Complete ==="
echo ""
echo "Next steps:"
echo "1. If this is a fresh install, set root password: mysql_secure_installation"
echo "2. Update application-macro.yml with your MySQL credentials"
echo "3. Run schema.sql to create tables: mysql -u root pios_macro < backend/src/main/resources/db/schema.sql"
echo ""
echo "To start application with MySQL profile:"
echo "  ./mvnw spring-boot:run -Dspring-boot.run.profiles=macro"
