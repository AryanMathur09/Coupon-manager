# --- Stage 1: Build the Application ---
# Use a Maven image with Java 21 to build the project
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy your source code into the container
COPY . .
# Build the JAR file (skip tests to save time during deployment)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
# Use a lightweight Java 21 runtime image
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar
# Expose the port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]