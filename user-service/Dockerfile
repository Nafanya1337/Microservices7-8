FROM gradle:7.6-jdk17 as builder
WORKDIR /app
COPY . /app
RUN ./gradlew build

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
