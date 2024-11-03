# Stage 1: Build the application
FROM gradle:7.6.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and settings files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download dependencies without running the full build
RUN ./gradlew build -x test --no-daemon || return 0

# Copy the rest of the application code and build the application
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Package the application
FROM openjdk:17-jdk-slim

# Set the working directory for the final stage
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
