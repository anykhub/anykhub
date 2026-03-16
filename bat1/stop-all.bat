@echo off
setlocal enabledelayedexpansion

echo.
echo ============================================
echo   Stopping RuoYi-Cloud Services
echo ============================================
echo.

call :stop_jar "ruoyi-gateway.jar"  "RuoYi-Gateway"
call :stop_jar "ruoyi-auth.jar"     "RuoYi-Auth"
call :stop_jar "ruoyi-modules-system.jar" "RuoYi-System"
call :stop_jar "ruoyi-modules-gen.jar"    "RuoYi-Gen"
call :stop_jar "ruoyi-modules-job.jar"    "RuoYi-Job"
call :stop_jar "ruoyi-modules-file.jar"   "RuoYi-File"
call :stop_jar "ruoyi-visual-monitor.jar" "RuoYi-Monitor"

:: Also try to stop FtpServer
echo [INFO] Stopping FtpServer ...
wmic process where "commandline like '%%org.apache.ftpserver.main.CommandLine%%' and name='java.exe'" call terminate >nul 2>&1
wmic process where "commandline like '%%ftpd.bat%%' and name='cmd.exe'" call terminate >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq FtpServer" /T >nul 2>&1

:: Also try to stop Redis and Nginx
echo [INFO] Stopping Redis and Nginx ...
taskkill /F /IM redis-server.exe /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Redis" /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Administrator: Redis" /T >nul 2>&1
wmic process where "name='cmd.exe' and commandline like '%%Redis%%'" call terminate >nul 2>&1
powershell -NoProfile -Command "try { $jv=Get-CimInstance Win32_Process -Filter \"Name='cmd.exe' AND CommandLine LIKE '%%Redis%%'\" -ErrorAction Stop } catch { $jv=Get-WmiObject Win32_Process -Filter \"Name='cmd.exe' AND CommandLine LIKE '%%Redis%%'\" } ; if($jv) { foreach($p in $jv) { try{ Stop-Process -Id $p.ProcessId -Force -ErrorAction SilentlyContinue }catch{} } }" >nul 2>&1

taskkill /F /IM nginx.exe /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Nginx" /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Administrator: Nginx" /T >nul 2>&1

:: Also try to stop Kafka and Zookeeper
echo [INFO] Stopping Kafka and Zookeeper ...
wmic process where "commandline like '%%kafka.Kafka%%' and name='java.exe'" call terminate >nul 2>&1
wmic process where "commandline like '%%zookeeper.server.quorum.QuorumPeerMain%%' and name='java.exe'" call terminate >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Kafka" /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Zookeeper" /T >nul 2>&1

:: Also try to stop Nacos
echo [INFO] Stopping Nacos ...
wmic process where "commandline like '%%nacos%%startup.cmd%%' and name='cmd.exe'" call terminate >nul 2>&1
wmic process where "commandline like '%%nacos%%nacos-server.jar%%' and name='java.exe'" call terminate >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Nacos" /T >nul 2>&1

echo.
echo  All services stopped!
echo.
pause
exit /b 0

:stop_jar
set JAR=%~1
set NAME=%~2
echo [INFO] Stopping %NAME% (%JAR%) ...

:: 1. Universal WMIC command (Works natively on Win7)
wmic process where "name='java.exe' and commandline like '%%%JAR%%%'" call terminate >nul 2>&1

:: 2. Win10/11 PowerShell style (Get-CimInstance) with fallback to Win7 (Get-WmiObject)
powershell -NoProfile -Command "try { $jv=Get-CimInstance Win32_Process -Filter \"Name='java.exe' AND CommandLine LIKE '%%java%%-jar%%%JAR%%%'\" -ErrorAction Stop } catch { $jv=Get-WmiObject Win32_Process -Filter \"Name='java.exe' AND CommandLine LIKE '%%java%%-jar%%%JAR%%%'\" } ; if($jv) { foreach($p in $jv){ try{ Stop-Process -Id $p.ParentProcessId -Force -ErrorAction SilentlyContinue }catch{}; try{ Stop-Process -Id $p.ProcessId -Force -ErrorAction SilentlyContinue }catch{} } }" >nul 2>&1

:: 3. Fallback window title kill (Handling Win7 Admin prefixes)
taskkill /F /FI "WINDOWTITLE eq %NAME%" /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Administrator:  %NAME%" /T >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Administrator: %NAME%" /T >nul 2>&1

exit /b 0