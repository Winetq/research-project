FROM maven:3.9.6-eclipse-temurin-17 AS compile

COPY pom.xml pom.xml
COPY src src

RUN mvn clean package

FROM eclipse-temurin:17-alpine AS package

COPY --from=compile target/*.jar /app/server.jar

WORKDIR /app

CMD ["java", "-jar", "server.jar"]