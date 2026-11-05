param(
    [Parameter(Mandatory=$true, Position=0)]
    [ValidateSet("start-db", "stop-db", "start-backend", "start-frontend", "test", "lint", "build", "docker-up", "docker-down", "logs", "status")]
    [string]$Action
)

$ErrorActionPreference = "Stop"
$RepoRoot = Split-Path -Parent $PSScriptRoot
$PgBinDir = "D:\DB_Design\PostgreSQL\pgsql\bin"
$PgDataDir = Join-Path $RepoRoot ".pgdata"
$PgLogFile = Join-Path $RepoRoot ".pgdata\postgres.log"

switch ($Action) {
    "start-db" {
        Write-Host "[SchoolConnect] Checking local PostgreSQL 17 cluster..." -ForegroundColor Cyan
        if (-not (Test-Path $PgDataDir)) {
            Write-Host "[SchoolConnect] Initializing local PostgreSQL cluster in $PgDataDir..." -ForegroundColor Yellow
            & "$PgBinDir\initdb.exe" -D $PgDataDir -U postgres -A trust -E UTF8 --no-locale
        }
        $status = & "$PgBinDir\pg_ctl.exe" -D $PgDataDir status 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "[SchoolConnect] Starting PostgreSQL on port 5432..." -ForegroundColor Green
            & "$PgBinDir\pg_ctl.exe" -D $PgDataDir -l $PgLogFile -o "-p 5432" start
            Start-Sleep -Seconds 2
        } else {
            Write-Host "[SchoolConnect] PostgreSQL is already running." -ForegroundColor Green
        }
        $dbExists = & "$PgBinDir\psql.exe" -h localhost -p 5432 -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='schoolconnect'"
        if ($dbExists -ne "1") {
            Write-Host "[SchoolConnect] Creating database 'schoolconnect'..." -ForegroundColor Yellow
            & "$PgBinDir\createdb.exe" -h localhost -p 5432 -U postgres schoolconnect
        }
        Write-Host "[SchoolConnect] Database 'schoolconnect' is ready on localhost:5432." -ForegroundColor Green
    }
    "stop-db" {
        if (Test-Path $PgDataDir) {
            & "$PgBinDir\pg_ctl.exe" -D $PgDataDir stop -m fast
        }
    }
    "start-backend" {
        Set-Location (Join-Path $RepoRoot "backend")
        .\gradlew.bat bootRun --no-daemon
    }
    "start-frontend" {
        Set-Location (Join-Path $RepoRoot "frontend")
        npm start
    }
    "test" {
        Write-Host "[SchoolConnect] Running Backend Tests..." -ForegroundColor Cyan
        Set-Location (Join-Path $RepoRoot "backend")
        .\gradlew.bat test --no-daemon
        Write-Host "[SchoolConnect] Running Frontend Tests..." -ForegroundColor Cyan
        Set-Location (Join-Path $RepoRoot "frontend")
        npm test -- --run
    }
    "lint" {
        Set-Location (Join-Path $RepoRoot "frontend")
        npm run lint
        npm run typecheck
    }
    "build" {
        Set-Location (Join-Path $RepoRoot "backend")
        .\gradlew.bat build --no-daemon
        Set-Location (Join-Path $RepoRoot "frontend")
        npm run build
    }
    "docker-up" {
        docker compose -f (Join-Path $RepoRoot "infra\docker-compose.yml") up -d --build
    }
    "docker-down" {
        docker compose -f (Join-Path $RepoRoot "infra\docker-compose.yml") down
    }
    "logs" {
        if (Test-Path $PgLogFile) {
            Get-Content $PgLogFile -Tail 50
        }
    }
    "status" {
        if (Test-Path $PgDataDir) {
            & "$PgBinDir\pg_ctl.exe" -D $PgDataDir status
        } else {
            Write-Host "Local PostgreSQL cluster not initialized yet."
        }
    }
}

