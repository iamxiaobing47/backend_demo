@echo off
setlocal

REM 停止后端服务
echo Stopping backend service...
for /f "tokens=5" %%i in ('netstat -ano ^| findstr :8080') do taskkill /PID %%i /F

echo Backend service stopped.
pause