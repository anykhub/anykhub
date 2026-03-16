@echo off
setlocal

set DIR=%~dp0
set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m
set WAIT=15

:: ---- FtpServer config ----
set FTP_DIR=C:\Users\Administrator\Desktop\ftpserver
set FTP_PORT=2121
set FTP_TIMEOUT=15

:: ---- Redis config ----
set REDIS_DIR=D:\Redis-x64-5.0.14.1
set REDIS_PORT=6379
set REDIS_TIMEOUT=20

:: ---- Nginx config ----
set NGINX_DIR=D:\soft\nginx
set NGINX_PORT=80
set NGINX_TIMEOUT=15

:: ---- Nacos config ----
:: Set the full path to Nacos startup.cmd
set NACOS_CMD=D:\soft\nacos\bin\startup.cmd
:: Nacos port to wait on
set NACOS_PORT=8848
:: Max seconds to wait for Nacos to be ready
set NACOS_TIMEOUT=120
:: Nacos Server Address (used to override application.yml/bootstrap.yml)
set NACOS_ADDR=127.0.0.1:%NACOS_PORT%

:: ---- Kafka config ----
:: Set the full path to Kafka bin\windows folder
set KAFKA_DIR=D:\soft\kafka\bin\windows
:: Kafka uses Zookeeper, so we start both. Adjust if using KRaft.
set ZK_PORT=2181
set KAFKA_PORT=9092
set KAFKA_TIMEOUT=60

echo.
echo ============================================
echo   RuoYi-Cloud Services Start (Serial)
echo   Dir: %DIR%
echo ============================================
echo.


:: ---------- Step 0.5: Start Redis ----------
if exist "%REDIS_DIR%\redis-server.exe" (
    echo [0.1/7] Starting Redis ...
    start "Redis" cmd /c "cd /d "%REDIS_DIR%" && title Redis && redis-server.exe redis.windows.conf"
    echo [INFO] Waiting for Redis port %REDIS_PORT% ...
    call :wait_port %REDIS_PORT% %REDIS_TIMEOUT%
    echo [INFO] Redis is ready.
) else (
    echo [WARN] Redis not found: %REDIS_DIR%\redis-server.exe
    echo        Skipping Redis startup...
)
echo.


:: ---------- Step 0.8: Start Apache FtpServer ----------
if exist "%FTP_DIR%\bin\ftpd.bat" (
    echo [0.8/7] Starting FtpServer ...
    start "FtpServer" cmd /c "cd /d "%FTP_DIR%" && title FtpServer && bin\ftpd.bat"
    echo [INFO] Waiting for FtpServer port %FTP_PORT% ...
    call :wait_port %FTP_PORT% %FTP_TIMEOUT%
    echo [INFO] FtpServer is ready.
) else (
    echo [WARN] FtpServer not found: %FTP_DIR%\bin\ftpd.bat
    echo        Skipping FtpServer startup...
)
echo.

:: ---------- Step 1: Start Nacos ----------
if not exist "%NACOS_CMD%" (
    echo [ERROR] Nacos not found: %NACOS_CMD%
    echo         Please set NACOS_CMD at the top of this script.
    pause
    exit /b 1
)
echo [0/7] Starting Nacos ...
start "Nacos" cmd /c ""%NACOS_CMD%""
echo [INFO] Nacos started, waiting for port %NACOS_PORT% (max %NACOS_TIMEOUT%s) ...

call :wait_port %NACOS_PORT% %NACOS_TIMEOUT%
if errorlevel 1 (
    echo [ERROR] Nacos did not come up within %NACOS_TIMEOUT%s. Aborting.
    pause
    exit /b 1
)
echo [INFO] Nacos is ready.
echo.

:: ---------- Step 1.5: Start Kafka & Zookeeper ----------
if exist "%KAFKA_DIR%\zookeeper-server-start.bat" (
    echo.
    echo [0.5/7] Starting Zookeeper ...
    start "Zookeeper" cmd /c "cd /d "%KAFKA_DIR%\.." && bin\windows\zookeeper-server-start.bat config\zookeeper.properties"
    echo [INFO] Waiting for Zookeeper port %ZK_PORT% ...
    call :wait_port %ZK_PORT% %KAFKA_TIMEOUT%

    echo [0.6/7] Starting Kafka ...
    start "Kafka" cmd /c "cd /d "%KAFKA_DIR%\.." && bin\windows\kafka-server-start.bat config\server.properties"
    echo [INFO] Waiting for Kafka port %KAFKA_PORT% ...
    call :wait_port %KAFKA_PORT% %KAFKA_TIMEOUT%
    echo [INFO] Kafka is ready.
) else (
    echo [WARN] Kafka directory not found or invalid: %KAFKA_DIR%
    echo        Skipping Kafka startup...
)
echo.

:: ---------- Step 2: Start microservices ----------
call :svc "RuoYi-Gateway" ruoyi-gateway.jar          1
call :svc "RuoYi-Auth"    ruoyi-auth.jar              2
call :svc "RuoYi-System"  ruoyi-modules-system.jar    3
::call :svc "RuoYi-Gen"     ruoyi-modules-gen.jar       4
::call :svc "RuoYi-Job"     ruoyi-modules-job.jar       5
call :svc "RuoYi-File"    ruoyi-modules-file.jar      6
::call :svc "RuoYi-Monitor" ruoyi-visual-monitor.jar    7

:: ---------- Step 2.5: Start other category jars ----------
:: 可以在这里调用新加的 :svc_other 方法来启动另一类 jar
:: 示例 (请根据实际情况取消注释并修改 jar 名称和编号):
:: call :svc_other "New-Service" new-service.jar 8

echo.

:: ---------- Step 3: Start Nginx ----------
if exist "%NGINX_DIR%\nginx.exe" (
    echo.
    echo [INFO] Starting Nginx ...
    start "Nginx" cmd /c "cd /d "%NGINX_DIR%" && title Nginx && nginx.exe"
    echo [INFO] Waiting for Nginx port %NGINX_PORT% ...
    call :wait_port %NGINX_PORT% %NGINX_TIMEOUT%
    echo [INFO] Nginx is ready.
) else (
    echo.
    echo [WARN] Nginx not found: %NGINX_DIR%\nginx.exe
    echo        Skipping Nginx startup...
)

echo.
echo  All services started!
echo.
pause
exit /b 0

:: ---- Wait until TCP port is open ----
:wait_port
set PORT=%1
set MAX=%2
set /a CNT=0
:wp_loop
powershell -NoProfile -Command "try { $t=New-Object Net.Sockets.TcpClient; $t.Connect('127.0.0.1',%PORT%); $t.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if not errorlevel 1 exit /b 0
set /a CNT=CNT+1
if %CNT% geq %MAX% exit /b 1
timeout /t 1 /nobreak >nul
goto wp_loop

:: ---- Start a single service ----
:svc
set T=%~1
set J=%DIR%%2
set S=%3

if not exist "%J%" (
    echo [ERROR] Not found: %J%
    echo         Run deploy.bat first.
    pause
    exit /b 1
)

echo [%S%/7] Starting %T% ...
start "%T%" cmd /c "title %T% && java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar "%J%" --spring.cloud.nacos.discovery.server-addr=%NACOS_ADDR% --spring.cloud.nacos.config.server-addr=%NACOS_ADDR%"
echo [INFO]  %T% started, waiting %WAIT%s ...

set /a N=%WAIT%
:w
if %N% leq 0 goto :eof
timeout /t 1 /nobreak >nul
set /a N=N-1
goto w

:: ---- Start a single service (Other category with different parameters) ----
:svc_other
set T=%~1
set J=%DIR%%2
set S=%3

if not exist "%J%" (
    echo [ERROR] Not found: %J%
    echo         Run deploy.bat first.
    pause
    exit /b 1
)

echo [%S%] Starting %T% ...
:: TODO: 请在下方的 start 命令中修改成您这类 jar 需要的独立参数
start "%T%" cmd /c "title %T% && java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar "%J%" --your.custom.parameter=value"
echo [INFO]  %T% started, waiting %WAIT%s ...

set /a N=%WAIT%
:w_other
if %N% leq 0 goto :eof
timeout /t 1 /nobreak >nul
set /a N=N-1
goto w_other