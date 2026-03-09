@echo off
echo Creating test users via API (properly encrypted)...
echo Make sure the backend server is running on port 8080
echo.

echo Creating business owner A...
curl -X POST http://localhost:8080/api/auth/create-test-user ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"owner_a@example.com\", \"password\":\"password123\"}"
echo.
echo.

echo Creating business owner B...
curl -X POST http://localhost:8080/api/auth/create-test-user ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"owner_b@example.com\", \"password\":\"password123\"}"
echo.
echo.

echo Creating staff at location X...
curl -X POST http://localhost:8080/api/auth/create-test-user ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"staff_x@example.com\", \"password\":\"password123\"}"
echo.
echo.

echo Creating staff at location Y...
curl -X POST http://localhost:8080/api/auth/create-test-user ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"staff_y@example.com\", \"password\":\"password123\"}"
echo.
echo.

echo Creating general test user...
curl -X POST http://localhost:8080/api/auth/create-test-user ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"test@example.com\", \"password\":\"password123\"}"
echo.
echo.

echo All test users created via API with properly encrypted passwords!
echo Now run the SQL script to create business/staff associations:
echo psql -U postgres -d backend_demo -f "RESTORE_TEST_DATA.sql"
echo.
echo Test accounts:
echo   Business Owner A: owner_a@example.com (Password: password123) - Access: "项目列表"
echo   Business Owner B: owner_b@example.com (Password: password123) - Access: "文件模板下载" 
echo   Staff at Location X: staff_x@example.com (Password: password123) - Access: ALL menus
echo   Staff at Location Y: staff_y@example.com (Password: password123) - Access: "项目列表" only
pause