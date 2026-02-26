#!/bin/bash

echo "=== MySQL Installation Monitor ==="
echo ""
echo "MySQL is being installed via Homebrew in the background."
echo "This may take 10-30 minutes depending on your network."
echo ""

while ! which mysql > /dev/null 2>&1; do
    if ls ~/Library/Caches/Homebrew/downloads/*.incomplete 2>/dev/null | grep -q mysql; then
        size=$(ls -lh ~/Library/Caches/Homebrew/downloads/*.incomplete 2>/dev/null | grep mysql | awk '{print $5}')
        echo "Downloading MySQL... ($size downloaded)"
    else
        echo "Installing MySQL..."
    fi
    sleep 10
done

echo ""
echo "MySQL installed successfully!"
mysql --version
echo ""

echo "Setting up auto-start..."
brew services start mysql
sleep 2

echo ""
echo "MySQL service status:"
brew services list | grep mysql

echo ""
echo "Creating database..."
mysql -u root -e "CREATE DATABASE IF NOT EXISTS pios_macro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || echo "Note: Database may already exist or root password needs to be configured"

echo ""
echo "=== Setup Complete ==="
echo ""
echo "Next steps:"
echo "1. If fresh install, run: mysql_secure_installation"
echo "2. Create tables: mysql -u root pios_macro < backend/src/main/resources/db/schema.sql"
echo "3. Start app: ./mvnw spring-boot:run -Dspring-boot.run.profiles=macro"
