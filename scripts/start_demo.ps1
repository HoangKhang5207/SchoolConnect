# ==============================================================================
# SchoolConnect -- Live Demo Ready Launcher (Self-Healing & Persistent)
# ==============================================================================
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$RepoRoot = "d:\Coding\TitKul_ProjectTest\SchoolConnect"
$PgBin = "D:\DB_Design\PostgreSQL\pgsql\bin\pg_ctl.exe"
$PgData = Join-Path $RepoRoot ".pgdata"
$BackendJar = Join-Path $RepoRoot "backend\build\libs\schoolconnect-backend-1.0.0.jar"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  SCHOOLCONNECT -- LIVE DEMO ENVIRONMENT LAUNCHER         " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Start PostgreSQL if not running (Detached to prevent Ctrl+C termination)
Write-Host "`n[1/3] Checking PostgreSQL Database..." -ForegroundColor Yellow
$pgStatus = & $PgBin -D $PgData status 2>&1
if ($pgStatus -match "no server running") {
    Write-Host "Starting PostgreSQL cluster (detached)..." -ForegroundColor Cyan
    Start-Process -FilePath $PgBin -ArgumentList @("-D", $PgData, "-l", (Join-Path $RepoRoot "postgres-server.log"), "start") -WindowStyle Hidden
    Start-Sleep -Seconds 3
} else {
    Write-Host "PostgreSQL is already active and accepting connections." -ForegroundColor Green
}

# 2. Check Backend & Database Health
Write-Host "`n[2/3] Checking Spring Boot Backend & Database Connection (Port 8080)..." -ForegroundColor Yellow
$backendHealthy = $false
try {
    $resp = Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/v1/health" -TimeoutSec 2 -ErrorAction Stop
    if ($resp.success -eq $true -and $resp.data.status -eq "UP" -and $resp.data.database -eq "UP") {
        $backendHealthy = $true
    }
} catch {}

if (-not $backendHealthy) {
    # If an existing backend is hung or in error state, release port 8080
    $p8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
    if ($p8080) {
        Write-Host "Releasing existing process on port 8080 (PID: $($p8080.OwningProcess))..." -ForegroundColor Yellow
        Stop-Process -Id $p8080.OwningProcess -Force -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 2
    }

    Write-Host "Launching Spring Boot Backend in a dedicated window..." -ForegroundColor Cyan
    Start-Process -FilePath "java" -ArgumentList "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", $BackendJar

    # Wait for health probe
    Write-Host "Waiting for Backend & Database probe to respond UP..." -NoNewline
    $ready = $false
    for ($i = 0; $i -lt 30; $i++) {
        Start-Sleep -Seconds 1
        Write-Host "." -NoNewline
        try {
            $resp = Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/v1/health" -TimeoutSec 2 -ErrorAction Stop
            if ($resp.success -eq $true -and $resp.data.status -eq "UP" -and $resp.data.database -eq "UP") {
                $ready = $true
                break
            }
        } catch {}
    }
    Write-Host ""
    if ($ready) {
        Write-Host "Spring Boot Backend & PostgreSQL are UP and Healthy on port 8080!" -ForegroundColor Green
    } else {
        Write-Host "Backend took longer than expected. Check the opened Java console window." -ForegroundColor Yellow
    }
} else {
    Write-Host "Spring Boot Backend & PostgreSQL are already UP and Healthy." -ForegroundColor Green
}

# 3. Start Frontend Simulator (Port 3000)
Write-Host "`n[3/3] Checking Frontend Web Simulator (Port 3000)..." -ForegroundColor Yellow
$p3000 = Get-NetTCPConnection -LocalPort 3000 -ErrorAction SilentlyContinue
if (-not $p3000) {
    Write-Host "Launching Frontend Web Simulator in a dedicated window..." -ForegroundColor Cyan
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$RepoRoot\frontend'; npm run preview -- --port 3000"
    Start-Sleep -Seconds 2
} else {
    Write-Host "Frontend is already active on port 3000." -ForegroundColor Green
}

# 4. Smoke Test Probe
Write-Host "`n[SMOKE CHECK] Probing Demo Parent Session..." -ForegroundColor Yellow
try {
    $demoAuth = Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/v1/auth/demo-session" -Method POST -ContentType "application/json" -Body '{"role":"PARENT"}' -TimeoutSec 3 -ErrorAction Stop
    if ($demoAuth.success -eq $true) {
        $parent = $demoAuth.data.user.fullName
        $child = $demoAuth.data.linkedChildren[0].fullName
        Write-Host "SUCCESS: Demo Session OK! Parent: $parent | Primary Child: $child" -ForegroundColor Green
    }
} catch {
    Write-Host "Smoke probe failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n==========================================================" -ForegroundColor Cyan
Write-Host "  SYSTEM READY FOR LIVE DEMO & PRESENTATION AT 15:00      " -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  - Local Web Simulator : http://localhost:3000" -ForegroundColor White
Write-Host "  - Local Backend API   : http://localhost:8080/api/v1" -ForegroundColor White
Write-Host "  - Swagger API Docs    : http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "==========================================================" -ForegroundColor Cyan
