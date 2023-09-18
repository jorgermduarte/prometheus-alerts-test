# Use an official Maven image as a builder
FROM maven:3.8.3-openjdk-17-slim AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the source code to the container
COPY . /app

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK image as a runtime
FROM openjdk:22-slim-bullseye

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR from the builder stage
COPY --from=builder /app/target/*SNAPSHOT.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080
