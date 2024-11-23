FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Копируем Gradle файлы и настройки
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY gradle/libs.versions.toml gradle/libs.versions.toml

# Копируем исходный код сервисов и модуля common
COPY user-service user-service
COPY common common

# Выполняем сборку только для user-service
RUN ./gradlew :user-service:build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем собранный JAR-файл
COPY --from=build /app/user-service/build/libs/user-service-all.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
