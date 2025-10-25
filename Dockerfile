FROM maven:3.9.6-eclipse-temurin-21 AS build
#Builder
WORKDIR /app
copy pom.xml .
copy src ./src
run mvn clean package -DskipTests
#Runner
FROM openjdk:21-jdk-slim
WORKDIR /app
copy --from=build /app/target/*.jar app.jar
expose 8080
ENTRYPOINT ["java","-jar","app.jar"]
