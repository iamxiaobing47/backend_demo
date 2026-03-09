@echo off
echo Setting up PostgreSQL database for dynamic menu management system...
echo.
echo Please make sure PostgreSQL server is running.
echo.
echo Executing schema setup...
psql -U postgres -d backend_demo -f "src/main/resources/db/postgresql_schema-setup.sql"
if %ERRORLEVEL% NEQ 0 (
  echo Schema setup failed. Attempting to create database...
  createdb -U postgres backend_demo
  psql -U postgres -d backend_demo -f "src/main/resources/db/postgresql_schema-setup.sql"
)

echo.
echo NOTE: You now need to create users via the API to ensure proper password encryption.
echo.
echo 1. Start the backend server first:
echo    gradlew bootRun
echo.
echo 2. Run the create-test-users-via-api.bat script to create users with encrypted passwords
echo.
echo 3. Then run the RESTORE_TEST_DATA.sql script to set up user associations
echo.
echo See PostgreSQL_Setup_Instructions_Updated.md for complete instructions.
echo.
echo Starting backend server...
start cmd /k "cd /d %~dp0 && gradlew bootRun"

echo Starting frontend server...
start cmd /k "cd /d %~dp0/../frontend_demo && npm run dev"

echo.
echo See PostgreSQL_Setup_Instructions_Updated.md for detailed setup instructions.
pause