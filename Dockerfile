# Multi-stage build for Spring Boot application

# Stage 1: Build
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Gradle
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.11-bin.zip && \
    unzip gradle-8.11-bin.zip && \
    mv gradle-8.11 /opt/gradle && \
    rm gradle-8.11-bin.zip

ENV PATH="/opt/gradle/bin:${PATH}"

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY gradlew.bat ./

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build application
RUN gradle build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
