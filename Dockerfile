# Этап сборки
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar
COPY --from=build /app/target/cards-shop-0.0.1-SNAPSHOT.jar app.jar

# Указываем порт
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
