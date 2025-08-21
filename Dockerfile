FROM maven:3.9.4-eclipse-temurin-17 AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM openjdk:22-jdk

COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar /auth-service.jar

ENTRYPOINT ["java", "-jar", "/auth-service.jar"]