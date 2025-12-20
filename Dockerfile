# Multi-stage build for Spring Boot application

# Stage 1: Build
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

# Copy gradle wrapper
COPY gradlew ./
COPY gradlew.bat ./
COPY gradle ./gradle

# Copy gradle files
COPY build.gradle settings.gradle ./

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build application
RUN gradle build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
