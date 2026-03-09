@echo off
echo Setting up test data for dynamic menu management system...

echo Creating database schema...
mysql -u root -p < src/main/resources/db/schema-setup.sql

echo Inserting test data...
mysql -u root -p < src/main/resources/db/insert-test-data.sql

echo Test data setup complete!
echo.
echo Test accounts:
echo   Business Owner A: owner_a@example.com
echo   Business Owner B: owner_b@example.com  
echo   Staff at Location X: staff_x@example.com
echo   Staff at Location Y: staff_y@example.com
echo.
echo Starting backend server...
cd /d "%~dp0"
start cmd /k "gradlew bootRun"

echo Starting frontend server...
start cmd /k "cd ../frontend_demo && npm run dev"

echo.
echo Testing guide available in TESTING_GUIDE.md
pause