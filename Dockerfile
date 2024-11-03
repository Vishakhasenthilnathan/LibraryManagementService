# Stage 1: Build the application
FROM openjdk:17-jdk AS build
WORKDIR /app

# Install xargs
RUN apt-get update && apt-get install -y xargs

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Set execution permission for the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew clean build -x test

# Stage 2: Create the final Docker image
FROM openjdk:17-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]