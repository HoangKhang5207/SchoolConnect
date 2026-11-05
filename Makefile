# ==============================================================================
# SchoolConnect — Unified Developer Experience Makefile
# ==============================================================================

.PHONY: help start-db stop-db start-backend start-frontend build test lint docker-up docker-down logs

help:
	@echo "SchoolConnect Developer Commands:"
	@echo "  make start-db        - Start PostgreSQL database (local or Docker)"
	@echo "  make stop-db         - Stop local PostgreSQL database"
	@echo "  make start-backend   - Run Spring Boot 3.x Backend (port 8080)"
	@echo "  make start-frontend  - Run React + TypeScript Mini App (port 3000)"
	@echo "  make test            - Execute Backend (JUnit 5) & Frontend (Vitest) tests"
	@echo "  make lint            - Execute Frontend ESLint & TypeScript typecheck"
	@echo "  make build           - Build Backend JAR & Frontend production bundle"
	@echo "  make docker-up       - Start full stack via Docker Compose"
	@echo "  make docker-down     - Stop Docker Compose stack"
	@echo "  make logs            - Tail Docker Compose service logs"

start-db:
	powershell -ExecutionPolicy Bypass -File ./scripts/dev.ps1 start-db

stop-db:
	powershell -ExecutionPolicy Bypass -File ./scripts/dev.ps1 stop-db

start-backend:
	cd backend && ./gradlew bootRun --no-daemon

start-frontend:
	cd frontend && npm start

test:
	cd backend && ./gradlew test --no-daemon
	cd frontend && npm test -- --run

lint:
	cd frontend && npm run lint && npm run typecheck

build:
	cd backend && ./gradlew build --no-daemon -x test
	cd frontend && npm run build

docker-up:
	docker compose -f infra/docker-compose.yml up -d --build

docker-down:
	docker compose -f infra/docker-compose.yml down

logs:
	docker compose -f infra/docker-compose.yml logs -f

