FROM maven:3.9.8 AS builder
WORKDIR /app

COPY pom.xml .
# Download dependencies first to leverage Docker cache
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:22-jdk-slim
WORKDIR /app

COPY --from=builder /app/target/ /app/target
RUN mv $(ls /app/target/*.jar | head -n 1) /app/ms-auth-gateway.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/ms-auth-gateway.jar"]
