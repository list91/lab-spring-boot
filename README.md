# Карточный Магазин (Cards Shop)

## Описание проекта
Бэкенд для минисайта карточного магазина, разработанный с использованием Spring Boot и PostgreSQL.

## Технологии
- Java 17
- Spring Boot 3.1.5
- PostgreSQL
- Docker
- Maven

## Функциональность
- CRUD операции для товаров
- CRUD операции для категорий
- Работа с корзиной
- Управление пользователями

## Установка и запуск

### Prerequisites
- Java 17
- Maven
- Docker

### Локальный запуск
1. Клонируйте репозиторий
2. Запустите `docker-compose up`
3. Приложение будет доступно на `http://localhost:8080`

## API Endpoints

### Товары
- `GET /api/products` - Получить все товары
- `GET /api/products/{id}` - Получить товар по ID
- `POST /api/products` - Создать товар
- `PUT /api/products/{id}` - Обновить товар
- `DELETE /api/products/{id}` - Удалить товар

### Категории
- `GET /api/categories` - Получить все категории
- `GET /api/categories/{id}` - Получить категорию по ID
- `POST /api/categories` - Создать категорию
- `PUT /api/categories/{id}` - Обновить категорию
- `DELETE /api/categories/{id}` - Удалить категорию

### Корзина
- `POST /api/cart/{userId}/add/{productId}` - Добавить товар в корзину
- `DELETE /api/cart/{userId}/remove/{productId}` - Удалить товар из корзины
- `GET /api/cart/{userId}` - Получить товары в корзине

### Пользователи
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `POST /api/users` - Создать пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

## Тестирование
Для запуска тестов выполните:
```
mvn test
```

## Лицензия
[Укажите лицензию]
