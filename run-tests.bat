@echo off
echo Starting backend server...
start cmd /k "cd /d %~dp0 && gradlew bootRun"

echo Starting frontend server...
start cmd /k "cd /d %~dp0/../frontend_demo && npm run dev"

echo.
echo Test accounts:
echo   Business Owner A: owner_a@example.com (can access "项目列表", password: password123)
echo   Business Owner B: owner_b@example.com (can access "文件模板下载", password: password123) 
echo   Staff at Location X: staff_x@example.com (can access ALL menus, password: password123)
echo   Staff at Location Y: staff_y@example.com (can access "项目列表" only, password: password123)
echo.
echo See TESTING_GUIDE.md for detailed testing instructions.
pause