@echo off
setlocal

REM 启动后端服务
echo Starting backend service...
start "Backend Demo" /B java -jar build\libs\backend_demo-0.0.1-SNAPSHOT.jar > backend.log 2>&1

echo Backend service started in background.
echo Check backend.log for output.
pause