# Этап сборки
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Копирование только pom.xml для кэширования зависимостей
COPY pom.xml .

# Настройка Maven для использования центрального репозитория
RUN mkdir -p /root/.m2 \
    && echo "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
             xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
             xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 \
             https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
             <mirrors> \
                 <mirror> \
                     <id>central</id> \
                     <name>Maven Central Repository</name> \
                     <url>https://repo.maven.apache.org/maven2</url> \
                     <mirrorOf>*</mirrorOf> \
                 </mirror> \
             </mirrors> \
             <proxies> \
             </proxies> \
           </settings>" > /root/.m2/settings.xml

# Загрузка зависимостей
RUN mvn dependency:go-offline

# Копирование исходного кода
COPY src ./src

# Сборка приложения без тестов
RUN mvn clean package -DskipTests

# Финальный этап
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копирование собранного jar-файла из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Открываем порт приложения
EXPOSE 8080

# Точка входа
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
