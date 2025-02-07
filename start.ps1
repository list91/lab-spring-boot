# Скрипт для сборки и запуска приложения в Docker

# Остановка и удаление существующих контейнеров
Write-Host "Остановка существующих контейнеров..." -ForegroundColor Yellow
docker-compose down

# Сборка приложения
Write-Host "Сборка Docker-образов..." -ForegroundColor Green
docker-compose build

# Запуск контейнеров
Write-Host "Запуск контейнеров..." -ForegroundColor Cyan
docker-compose up -d

# Проверка статуса контейнеров
Write-Host "Проверка статуса контейнеров..." -ForegroundColor Magenta
docker-compose ps

# Просмотр логов
Write-Host "Показ логов приложения..." -ForegroundColor Blue
docker-compose logs -f
