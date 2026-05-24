# Pornește toate microserviciile + frontend (necesită Java 17+, Maven, Gradle, Node.js, MySQL)
$ErrorActionPreference = "Continue"
$root = $PSScriptRoot

Write-Host "=== Casa Productie - Pornire servicii ===" -ForegroundColor Cyan
Write-Host "Asigură-te că MySQL rulează și ai executat sql/init-databases.sql" -ForegroundColor Yellow

function Start-InWindow($title, $command, $workDir) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$workDir'; Write-Host '[$title]' -ForegroundColor Green; $command"
    Start-Sleep -Seconds 2
}

# 1. User Service (5230)
Start-InWindow "UserService" "mvn spring-boot:run" "$root\demo"

# 2. Film Service (5174)
Start-InWindow "FilmService" "mvn spring-boot:run" "$root\film.service"

# 3. Actor Service (5237)
Start-InWindow "ActorService" ".\gradlew.bat bootRun" "$root\actor-service"

# 4. Statistics Service (5238)
Start-InWindow "StatisticsService" ".\gradlew.bat bootRun" "$root\statistics-service"

Start-Sleep -Seconds 8

# 5. API Gateway (8080)
Start-InWindow "ApiGateway" ".\gradlew.bat bootRun" "$root\API-gataway"

Start-Sleep -Seconds 5

# 6. React Frontend (3000)
Start-InWindow "Frontend" "npm start" "$root\casa-productie-frontend"

Write-Host ""
Write-Host "Servicii pornite în ferestre separate." -ForegroundColor Green
Write-Host "Frontend: http://localhost:3000" -ForegroundColor Green
Write-Host "Gateway:  http://localhost:8080" -ForegroundColor Green
Write-Host ""
Write-Host "Conturi demo:" -ForegroundColor Yellow
Write-Host "  angajat@firma.ro / angajat123"
Write-Host "  manager@firma.ro / manager123"
Write-Host "  admin@firma.ro / admin123"
