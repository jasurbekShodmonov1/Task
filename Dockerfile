
FROM openjdk:21-jdk-slim

# Create and set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/task-0.0.1-SNAPSHOT.jar app.jar

# Expose port used by Spring Boot
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
