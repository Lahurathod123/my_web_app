FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy your JAR file into the container
COPY target/SpringBootApp-1.0.0.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
