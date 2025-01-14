# Base image for Java
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR file
COPY target/your-app.jar app.jar

# Expose the backend port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
