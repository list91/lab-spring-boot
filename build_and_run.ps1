# Script for building and running the application

# Stop existing containers
Write-Host "1. Stopping existing containers..." -ForegroundColor Cyan
docker-compose down

# Clean old images
Write-Host "`n2. Cleaning old images..." -ForegroundColor Cyan
docker rmi cards-shop-spring_app:latest
docker rmi cards-shop-spring_postgres:latest

# Build images
Write-Host "`n3. Building Docker images..." -ForegroundColor Cyan
docker-compose build

# Run containers
Write-Host "`n4. Running containers..." -ForegroundColor Cyan
docker-compose up -d

# Check container status
Write-Host "`n5. Container status:" -ForegroundColor Cyan
docker-compose ps

# View application logs
Write-Host "`n6. Application logs:" -ForegroundColor Cyan
docker-compose logs app

# Pause to view logs
Write-Host "`nApplication is running. Press any key to continue..." -ForegroundColor Green
$host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") | Out-Null

# Run test requests
Write-Host "`n7. Running test requests..." -ForegroundColor Cyan
.\test_products.ps1
