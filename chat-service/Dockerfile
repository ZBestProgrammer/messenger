FROM maven:3.9.4-eclipse-temurin-17 AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM openjdk:22-jdk

COPY --from=build /app/target/chat-service-0.0.1-SNAPSHOT.jar /chat-service.jar

ENTRYPOINT ["java", "-jar", "/chat-service.jar"]