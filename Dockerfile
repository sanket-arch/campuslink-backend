# Use Eclipse Temurin OpenJDK 17 as base image
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy built jar from target directory
COPY target/*.jar app.jar

# Expose port (change if your app uses a different port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
